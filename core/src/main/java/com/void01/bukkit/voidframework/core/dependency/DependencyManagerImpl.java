package com.void01.bukkit.voidframework.core.dependency;

import com.void01.bukkit.voidframework.api.common.dependency.Dependency;
import com.void01.bukkit.voidframework.api.common.dependency.DependencyManager;
import com.void01.bukkit.voidframework.api.common.dependency.Relocation;
import com.void01.bukkit.voidframework.api.common.dependency.Repository;
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin;
import com.void01.bukkit.voidframework.core.dependency.exception.FileChecksumMismatchException;
import com.void01.bukkit.voidframework.core.dependency.exception.FileChecksumException;
import com.void01.bukkit.voidframework.core.dependency.exception.FileDownloadException;
import com.void01.bukkit.voidframework.core.dependency.util.FileUtils;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DependencyManagerImpl implements DependencyManager {
    private final Logger logger;
    private final DependencyFileHelper dependencyFileHelper;

    public DependencyManagerImpl(@NonNull VoidFrameworkPlugin plugin) {
        this.logger = plugin.getLogger();
        this.dependencyFileHelper = new DependencyFileHelper(plugin);
    }

    @Override
    public void loadDependency(@NonNull Plugin plugin, @NonNull Dependency dependency, @NonNull List<Repository> repositories, @NonNull List<Relocation> relocations, boolean recursively) {
        if (repositories.contains(null)) {
            throw new IllegalArgumentException("repositories can not contains null");
        }

        if (relocations.contains(null)) {
            throw new IllegalArgumentException("repositories can not contains null");
        }

        if (repositories.isEmpty()) {
            throw new IllegalArgumentException("repositories cannot be empty");
        }

        loadDependency0(plugin, dependency, repositories, relocations, recursively, 0);
    }

    private void loadDependency0(@NonNull Plugin plugin, @NonNull Dependency dependency, @NonNull List<Repository> repositories, @NonNull List<Relocation> relocations, boolean recursively, int level) {
        logger.info(String.format("Loading dependency: %s (level: %d)", dependency.getAsGradleStyleExpression(), level));

        downloadAndVerifyChecksumFromRepositoriesIfNecessary(dependency, DependencyFileType.JAR, repositories);
        File jarFile = dependencyFileHelper.getDependencyMainFile(dependency, DependencyFileType.JAR);

        // 有的依赖没有 jar，而是 pom 里一大堆
        if (jarFile.exists()) {
            // 如果有重定向则重定向
            if (!relocations.isEmpty()) {
                jarFile = DependencyRelocator.relocate(jarFile, relocations);
            }

            DependencyInjector.inject(plugin.getClass().getClassLoader(), jarFile); // 注入路径
        }

        if (recursively) {
            // POM
            downloadAndVerifyChecksumFromRepositoriesIfNecessary(dependency, DependencyFileType.POM, repositories);
            DependencyPomParser.parseCompileDependencies(dependencyFileHelper.getDependencyMainFile(dependency, DependencyFileType.POM))
                    .forEach(subDependency -> loadDependency0(plugin, subDependency, repositories, relocations, recursively, level + 1));
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
    private void downloadAndVerifyChecksumFromRepositoriesIfNecessary(Dependency dependency, DependencyFileType type, List<Repository> repositories) {
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
                downloadAndVerifyChecksumFromUrl(mainUrl, mainFile, md5Url, md5File);
                return;
            } catch (FileDownloadException | FileChecksumException ex) {
                exceptions.add(ex);
            }
        }

        exceptions.forEach(Throwable::printStackTrace);
    }

    /**
     * 下载并对比校验和
     */
    private void downloadAndVerifyChecksumFromUrl(String mainUrl, File mainDest, String md5Url, File md5Dest) {
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
            FileUtils.downloadFromUrl(jarUrl, jarDest, true);
        } catch (Exception ex) {
            logger.info("Download failed.");
            throw new FileDownloadException(ex);
        }
    }
}
