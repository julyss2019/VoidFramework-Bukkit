package com.void01.bukkit.voidframework.core.dependency.exception;

import lombok.NonNull;

import java.io.File;

public class FileChecksumMismatchException extends FileChecksumException {
    public FileChecksumMismatchException(@NonNull File file, @NonNull String exceptedMd5, @NonNull String actualMd5) {
        super(String.format("\nFile %s MD5 mismatch\nexcepted: %s, actual: %s", file.getAbsolutePath(), exceptedMd5, actualMd5));
    }
}
