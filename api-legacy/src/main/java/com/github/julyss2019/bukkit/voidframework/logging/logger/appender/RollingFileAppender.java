package com.github.julyss2019.bukkit.voidframework.logging.logger.appender;

import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.internal.RollingFileNamePatternConverter;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.Layout;
import lombok.NonNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class RollingFileAppender extends FileAppender {
    private String rollingFileNamePattern;
    private boolean compress;

    /**
     * 周期文件追加器
     * 实现了以下功能：有一个主文件 latest.log, 次日可自动更替为 yyyy-MM-dd.log, 周而复始
     *
     * @param layout                 布局
     * @param level                  日志等级
     * @param file                   主文件
     * @param flushInterval          刷新间隔
     * @param rollingFileNamePattern 周期文件格式
     * @param compress               是否压缩
     */
    public RollingFileAppender(@NonNull Layout layout, @NonNull Level level, @NonNull File file, int flushInterval, @NonNull String rollingFileNamePattern, boolean compress) {
        super(layout, level, file, flushInterval);

        setCompress(compress);
        setRollingFileNamePattern(rollingFileNamePattern);
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }


    public String getRollingFileNamePattern() {
        return rollingFileNamePattern;
    }

    public void setRollingFileNamePattern(@NonNull String rollingFileNamePattern) {
        this.rollingFileNamePattern = rollingFileNamePattern;
    }

    /**
     * 更替文件
     */
    private void roll() {
        File file = getFile();

        // 没文件跳过
        if (!file.exists()) {
            return;
        }

        BasicFileAttributes fileAttributes;

        try {
            fileAttributes = Files.readAttributes(getFile().toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDate nowDate = LocalDate.now();
        LocalDate fileCreateDate = Instant.ofEpochMilli(fileAttributes.creationTime().toMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
        String newFileName = RollingFileNamePatternConverter.convert(rollingFileNamePattern, nowDate);
        String oldFileName = RollingFileNamePatternConverter.convert(rollingFileNamePattern, fileCreateDate);

        if (!newFileName.equals(oldFileName)) {
            closeWriter();

            File rolledFile = getFile(); // 被 roll 的文件, latest.log

            if (compress) {
                File destFile = new File(rolledFile.getParentFile() + File.separator + oldFileName + ".zip");

                try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(destFile.toPath()))) {
                    ZipEntry entry = new ZipEntry(rolledFile.getName());

                    zipOut.putNextEntry(entry);

                    InputStream rolledFileInput = Files.newInputStream(rolledFile.toPath());
                    BufferedInputStream bufferedRolledFileInput = new BufferedInputStream(rolledFileInput);
                    byte[] buffer = new byte[1024];
                    int len;

                    while ((len = bufferedRolledFileInput.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, len);
                    }

                    zipOut.closeEntry();

                    if (!rolledFile.delete()) {
                        throw new RuntimeException("delete file failed: " + rolledFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                File destFile = new File(rolledFile.getParentFile() + File.separator + oldFileName);

                if (!rolledFile.renameTo(destFile)) {
                    throw new RuntimeException("rename file " + rolledFile.getAbsolutePath() + " to " + destFile.getAbsolutePath() + " failed");
                }
            }
        }
    }

    @Override
    public void append(@NonNull MessageContext messageContext) {
        synchronized (this) {
            roll();
            // 使用 FileAppender 实现
            super.append(messageContext);
        }
    }
}
