package com.github.julyss2019.bukkit.voidframework.logging.logger.appender;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.Layout;
import lombok.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;

/**
 * 文件追加器
 */
public class FileAppender extends BaseAppender {
    private File file;
    private BufferedWriter bufferedWriter;
    private int flushInterval;

    public FileAppender(@NonNull Layout layout, @NonNull Level threshold, @NonNull File file, int flushInterval) {
        super(layout, threshold);

        setFile(file);
        setFlushInterval(flushInterval);
    }

    public int getFlushInterval() {
        return flushInterval;
    }

    public File getFile() {
        return file;
    }

    public void setFlushInterval(int flushInterval) {
        Validator.checkState(flushInterval >= 0, "flushInterval must >= 0");

        this.flushInterval = flushInterval;
    }

    public void setFile(@NonNull File file) {
        if (!file.equals(this.file)) {
            closeWriter(); // 关闭老的
            this.file = file;
        }
    }

    /**
     * 打开写入器
     */
    private void setNewWriter() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            File parent = file.getParentFile();

            if (!parent.exists() && !parent.mkdirs()) {
                throw new RuntimeException("mkdirs failed: " + parent.getAbsolutePath());
            }

            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("create new file failed: " + file.getAbsolutePath());
                }

                // https://stackoverflow.com/questions/20884521/date-created-is-not-going-to-change-while-delete-file-and-then-create-file
                // 解决关于在 Windows 删除文件后再创建，文件创建时间不更新的问题
                Files.setAttribute(file.toPath(), "creationTime", FileTime.fromMillis(System.currentTimeMillis()), LinkOption.NOFOLLOW_LINKS);
                this.bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            } else {
                this.bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写到磁盘
     */
    public void flush() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 写到缓存
     * @param line 行
     */
    protected void write(@NonNull String line) {
        if (bufferedWriter == null) {
            setNewWriter();
        }

        try {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭写入器
     */
    protected void closeWriter() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
                bufferedWriter = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        super.close();

        closeWriter();
    }

    @Override
    public void append(@NonNull MessageContext messageContext) {
        synchronized (this) {
            super.append(messageContext);
            write(getLayout().format(messageContext));
        }
    }
}
