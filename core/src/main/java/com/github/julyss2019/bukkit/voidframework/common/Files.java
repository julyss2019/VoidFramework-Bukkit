package com.github.julyss2019.bukkit.voidframework.common;

import lombok.NonNull;

import java.io.File;

public class Files {
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

    public static void deleteFolder(@NonNull  File folder) {
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
