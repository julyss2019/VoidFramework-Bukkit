package com.github.julyss2019.bukkit.voidframework.dependency;

import com.void01.dependencymanager.internal.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DependencyDownloader {
    private final List<Repository> repositories = new ArrayList<>();

    public DependencyDownloader() {
        repositories.add(new Repository("https://maven.aliyun.com/repository/public"));
        repositories.add(new Repository("https://repo1.maven.org/maven2"));
    }

    public void download(Dependency dependency, File dir) {
        boolean download = false;

        for (Repository repository : repositories) {
            try {
                checkSha1(download0(repository.getJarSha1Url(dependency), dir), download0(repository.getJarUrl(dependency), dir));
                checkSha1(download0(repository.getPomSha1Url(dependency), dir), download0(repository.getPomUrl(dependency), dir));
                download = true;
                break;
            } catch (Exception ex) {
                if (ex.getCause() instanceof FileNotFoundException) {
                    continue;
                }

                throw new RuntimeException(ex);
            }
        }

        if (!download) {
            throw new RuntimeException("download dependency failed: " + dependency);
        }
    }

    private File download0(String url, File dir) {
        File file = new File(dir, url.substring(url.lastIndexOf("/") + 1));

        System.out.println("Downloading: " + url);
        FileUtils.download(url, file);
        return file;
    }

    private void checkSha1(File sha1File, File targetFile) {
        String sha1;

        try {
            List<String> lines = Files.readAllLines(sha1File.toPath());

            if (lines.isEmpty()) {
                throw new RuntimeException("invalid sha1 file: " + sha1File.getAbsolutePath());
            }

            sha1 = lines.get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!FileUtils.getSha1(targetFile).equalsIgnoreCase(sha1)) {
            throw new RuntimeException("SHA-1 not match, file: " + targetFile.getAbsolutePath() + ", SHA-1: " + sha1);
        }
    }

    public static void main(String[] args) {
        DependencyDownloader dependencyDownloader = new DependencyDownloader();

        dependencyDownloader.download(Dependency.createByGradleCoordinates("org.apac1he.groovy:groovy:4.0.14"), new File("./"));
    }
}
