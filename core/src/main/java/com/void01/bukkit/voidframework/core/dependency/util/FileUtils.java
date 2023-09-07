package com.void01.bukkit.voidframework.core.dependency.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;

public class FileUtils {
    @SneakyThrows
    public static void downloadFromUrl(@NonNull String urlStr, @NonNull File dest, boolean overwrite) {
        if (dest.exists() && !overwrite) {
            return;
        }

        mkdirs(dest.getParentFile());

        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());

        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    @SneakyThrows
    public static String readFirstLine(@NonNull File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }


    public static String getNameWithoutExtension(@NonNull File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf(".");

        if (lastDot == -1) {
            return name;
        }

        return name.substring(0, lastDot);
    }

    public static void mkdirs(@NonNull File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("mkdirs failed: " + file.getAbsolutePath());
        }
    }

    @SneakyThrows
    public static String getMd5Checksum(@NonNull File file) {
        MessageDigest md = MessageDigest.getInstance("MD5");

        try (FileInputStream inputStream = new FileInputStream(file);) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }

            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }

            return sb.toString();
        }
    }
}
