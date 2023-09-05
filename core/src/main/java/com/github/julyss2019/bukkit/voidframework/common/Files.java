package com.github.julyss2019.bukkit.voidframework.common;

import lombok.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;

public class Files {
    public static String getSha1(@NonNull File file) {
        if (!file.exists()) {
            throw new RuntimeException("file not exists: " + file.getAbsolutePath());
        }

        try (FileInputStream input = new FileInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }

            // 计算哈希值
            byte[] sha1Hash = messageDigest.digest();
            StringBuilder hexString = new StringBuilder();

            for (byte b : sha1Hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void download(String url, File dest) {
        try {
            URL javaUrl = new URL(url);
            ReadableByteChannel channel = Channels.newChannel(javaUrl.openStream());

            try (FileOutputStream out = new FileOutputStream(dest)) {
                out.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mkdirs(@NonNull File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("mkdirs failed: " + file.getAbsolutePath());
        }
    }

    public static String getFileExtension(@NonNull File file) {
        String absolutePath = file.getAbsolutePath();
        int index = absolutePath.lastIndexOf(".");

        if (index == -1) {
            return null;
        }

        return absolutePath.substring(index + 1);
    }

    public static void deleteFolder(@NonNull File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    deleteFile(file);
                }
            }
        }

        deleteFile(folder);
    }

    private static void deleteFile(@NonNull File file) {
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("delete file failed: " + file.getAbsolutePath());
        }
    }
}
