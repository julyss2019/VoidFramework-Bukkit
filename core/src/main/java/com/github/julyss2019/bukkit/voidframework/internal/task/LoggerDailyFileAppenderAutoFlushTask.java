package com.github.julyss2019.bukkit.voidframework.internal.task;



import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.logging.LogManager;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.FileAppender;
import com.github.julyss2019.bukkit.voidframework.task.QueuedBukkitRunnable;
import lombok.NonNull;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LoggerDailyFileAppenderAutoFlushTask extends QueuedBukkitRunnable {
    private final LogManager logManager;
    private final Map<FileAppender, Long> lastFlushedMap = new HashMap<>();

    public LoggerDailyFileAppenderAutoFlushTask(@NonNull VoidFrameworkPlugin plugin) {
        this.logManager = plugin.getLogManager();
    }

    @Override
    public void queuedRun() {
        logManager.getLoggers().forEach(logger -> {
            logger.getAppenders().forEach(appender -> {
                if (appender instanceof FileAppender) {
                    FileAppender fileAppender = (FileAppender) appender;

                    if (!lastFlushedMap.containsKey(fileAppender)) {
                        lastFlushedMap.put(fileAppender, System.currentTimeMillis());
                    }

                    if (System.currentTimeMillis() - lastFlushedMap.get(fileAppender) >= fileAppender.getFlushInterval() * 1000L) {
                        fileAppender.flush();
                        lastFlushedMap.put(fileAppender, System.currentTimeMillis());
                    }
                }
            });
        });
    }
}
