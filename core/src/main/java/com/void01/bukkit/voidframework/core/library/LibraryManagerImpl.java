package com.void01.bukkit.voidframework.core.library;

import com.void01.bukkit.voidframework.api.common.library.*;
import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import com.void01.bukkit.voidframework.common.UrlClassLoaderModifier;
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin;
import com.void01.bukkit.voidframework.core.library.exception.FileChecksumException;
import com.void01.bukkit.voidframework.core.library.exception.FileChecksumMismatchException;
import com.void01.bukkit.voidframework.core.library.exception.FileDownloadException;
import com.void01.bukkit.voidframework.core.library.relocate.DependencyRelocator;
import com.void01.bukkit.voidframework.core.library.util.FileUtils;
import lombok.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class LibraryManagerImpl implements LibraryManager {
    private final Logger logger;
    private final DependencyFileHelper dependencyFileHelper;
    private DependencyRelocator dependencyRelocator;

    public LibraryManagerImpl(@NonNull VoidFrameworkPlugin plugin) {
        this.logger = plugin.getLogger();
        this.dependencyFileHelper = new DependencyFileHelper(plugin);

        loadDependencyRelocator();
    }

    private void loadDependencyRelocator() {
        try {
            IsolatedClassLoader isolatedClassLoader = new IsolatedClassLoader(null);
            loadDependency(Dependency.fromGradleStyleExpression("me.lucko:jar-relocator:1.7"),
                    isolatedClassLoader,
                    Arrays.asList(Repository.ALIYUN, Repository.CENTRAL),
                    Collections.emptyList(),
                    true
            );
            this.dependencyRelocator = new DependencyRelocator(
                    isolatedClassLoader.loadClass("me.lucko.jarrelocator.JarRelocator"),
                    isolatedClassLoader.loadClass("me.lucko.jarrelocator.Relocation"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadDependency(@NonNull Dependency dependency,
                               @NonNull ClassLoader classLoader,
                               @NonNull List<Repository> repositories,
                               @NonNull List<Relocation> relocations,
                               boolean loadRecursively) {
        load0(dependency, classLoader, repositories, relocations, loadRecursively, 0);
    }

    @Deprecated
    @Override
    public void load(@NonNull Library library) {
        if (library.getRepositories().isEmpty()) {
            throw new IllegalArgumentException("repositories can not be empty");
        }

        load0(library.getDependency(), library.getClassLoader(), library.getRepositories(), library.getRelocations(), library.isResolveRecursively(), 0);
    }

    private void load0(@NonNull Dependency dependency,
                       @NonNull ClassLoader classLoader,
                       @NonNull List<Repository> repositories,
                       @NonNull List<Relocation> relocations,
                       boolean loadRecursively,
                       int level) {
        logger.info(String.format("Loading dependency %s(level: %d) for %s.", dependency.getAsGradleStyleExpression(), level, classLoader));

        downloadDependency(dependency, DependencyFileType.JAR, repositories);

        File jarFile = dependencyFileHelper.getDependencyMainFile(dependency, DependencyFileType.JAR);

        // 如果有重定向则重定向
        if (!relocations.isEmpty()) {
            jarFile = dependencyRelocator.relocate(jarFile, relocations);
        }

        UrlClassLoaderModifier.addUrl(classLoader, jarFile); // 注入路径

        if (loadRecursively) {
            // POM
            downloadDependency(dependency, DependencyFileType.POM, repositories);
            DependencyPomParser.parseCompileDependencies(dependencyFileHelper.getDependencyMainFile(dependency, DependencyFileType.POM))
                    .forEach(subDependency -> {
                        load0(subDependency, classLoader, repositories, relocations, true, level + 1);
                    });
        }
    }

    /**
     * 检查是否需要下载
     */
    private boolean isNeedDownload(Dependency dependency, DependencyFileType type) {
        File mainFile = dependencyFileHelper.getDependencyMainFile(dependency, type);
        File md5File = dependencyFileHelper.getDependencyMd5File(dependency, type);

        boolean needDownloadJar = false;

        if (mainFile.exists() && md5File.exists()) {
            try {
                verifyChecksum(mainFile, md5File);
            } catch (FileChecksumException ex) {
                needDownloadJar = true;
            }
        } else {
            needDownloadJar = true;
        }

        return needDownloadJar;
    }

    /**
     * 从多个仓库下载依赖，如果所有仓库都没下载成功，则抛出异常
     */
    private void downloadDependency(Dependency dependency, DependencyFileType type, List<Repository> repositories) {
        if (!isNeedDownload(dependency, type)) {
            return;
        }

        List<Exception> exceptions = new ArrayList<>();

        for (Repository repository : repositories) {
            File mainFile = dependencyFileHelper.getDependencyMainFile(dependency, type);
            File md5File = dependencyFileHelper.getDependencyMd5File(dependency, type);
            String mainUrl = DependencyUrlGenerator.generateMainFileUrl(dependency, repository, type);
            String md5Url = DependencyUrlGenerator.generateMd5FileUrl(dependency, repository, type);

            try {
                downloadFile(mainUrl, mainFile, md5Url, md5File);
                return;
            } catch (FileDownloadException | FileChecksumException ex) {
                exceptions.add(ex);
            }
        }

        exceptions.forEach(Throwable::printStackTrace);
        throw new RuntimeException("download failed");
    }

    /**
     * 下载并对比校验和
     */
    private void downloadFile(String mainUrl, File mainDest, String md5Url, File md5Dest) {
        download(md5Url, md5Dest);
        download(mainUrl, mainDest);
        verifyChecksum(mainDest, md5Dest);
    }

    /**
     * 对比校验和
     */
    private void verifyChecksum(File mainFile, File md5File) {
        String exceptedMd5 = FileUtils.readFirstLine(md5File);

        if (exceptedMd5 == null) {
            throw new FileChecksumException("Empty MD5 file: " + md5File.getAbsolutePath());
        }

        String actualMd5 = FileUtils.getMd5Checksum(mainFile);

        if (!actualMd5.equals(exceptedMd5)) {
            throw new FileChecksumMismatchException(mainFile, exceptedMd5, actualMd5);
        }
    }

    /**
     * 下载
     *
     * @param jarUrl
     * @param jarDest
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private void download(String jarUrl, File jarDest) {
        logger.info(String.format("Downloading...(%s)", jarUrl));

        try {
            FileUtils.downloadFromUrlIf200Response(jarUrl, jarDest, true);
        } catch (Exception ex) {
            throw new FileDownloadException(ex);
        }
    }
}
