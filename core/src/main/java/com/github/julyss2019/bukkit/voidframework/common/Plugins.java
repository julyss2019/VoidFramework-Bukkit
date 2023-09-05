package com.github.julyss2019.bukkit.voidframework.common;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Plugins {

    /**
     * 保存文件资源
     *
     * @param plugin     插件
     * @param path       源路径(e.g config.yml, mobs), 所有系统统一使用 / 作为路径分隔符
     * @param destFolder 目标文件夹
     * @param overwrite  覆写
     */
    public static void savePluginResource(@NonNull Plugin plugin, @NonNull String path, @NonNull File destFolder, boolean overwrite) {
        // 修正
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (!destFolder.exists() && !destFolder.mkdirs()) {
            throw new RuntimeException("mkdirs failed: " + destFolder.getAbsolutePath());
        }

        // 这样才能转换为正确的路径, plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() 无法正确解析非英文的路径
        try (JarFile jarFile = new JarFile(new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation()
                .toURI()))) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String jarFilePath = entry.getName();

                if (jarFilePath.endsWith("/")) {
                    continue;
                }

                // 同时匹配文件夹和文件
                boolean isJarDir = jarFilePath.startsWith(path + "/");

                if (isJarDir || path.equals(jarFilePath)) {
                    InputStream inputStream = jarFile.getInputStream(entry);
                    String fileName = jarFilePath.substring(path.length());
                    File destFile = new File(destFolder, fileName);

                    if (!destFile.exists() || overwrite) {
                        File parentFile = destFile.getParentFile();

                        if (parentFile != null) {
                            Files.mkdirs(parentFile);
                        }

                        try (FileOutputStream fileOutputStream = new FileOutputStream(destFile)) {
                            int read;
                            byte[] buffer = new byte[1024];

                            while ((read = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, read);
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
