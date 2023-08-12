package com.github.julyss2019.bukkit.voidframework.logging;

import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.Appender;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.ConsoleAppender;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.RollingFileAppender;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.PatternLayout;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LogManager {
    private final VoidFrameworkPlugin plugin;
    private final Set<Logger> loggers = new HashSet<>();

    public LogManager(@NonNull VoidFrameworkPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 注销所有日志记录器
     */
    public void unregisterAllLoggers() {
        loggers.forEach(this::unregisterLogger0);
    }

    public void unregisterLoggers(@NonNull Plugin plugin) {
        Iterator<Logger> iterator = loggers.iterator();

        while (iterator.hasNext()) {
            Logger next = iterator.next();

            if (next.getHolder().equals(plugin)) {
                next.getAppenders().forEach(Appender::close);
                iterator.remove();
            }
        }
    }

    /**
     * 注销日志记录器
     *
     * @param logger 日志记录器
     */
    public void unregisterLogger(@NonNull Logger logger) {
        unregisterLogger0(logger);
        loggers.remove(logger);
    }

    /**
     * 获取所有日志记录器
     */
    public Set<Logger> getLoggers() {
        return Collections.unmodifiableSet(loggers);
    }

    /**
     * 创建一个简单日志记录器
     * 包含默认的：ConsoleAppender, RollingFileAppender
     *
     * @param plugin 插件
     */
    public Logger createSimpleLogger(@NonNull Plugin plugin) {
        Logger logger = createLogger(plugin);

        logger.addAppender(new ConsoleAppender(new PatternLayout("[%p] [%l] %m"), Level.INFO));
        logger.addAppender(new RollingFileAppender(
                new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}] [%l] %m"),
                Level.DEBUG,
                new File(plugin.getDataFolder(), "logs" + File.separator + "latest.log"),
                3,
                "%d{yyyy-MM-dd}.log",
                true));
        return logger;
    }

    /**
     * 创建一个日志记录器
     *
     * @param holder 插件
     */
    public Logger createLogger(@NonNull Plugin holder) {
        Logger logger = new Logger(plugin, holder);

        loggers.add(logger);
        return logger;
    }

    public Logger loadLogger(@NonNull Section section) {
        throw new UnsupportedOperationException();
    }

    /**
     * 注销日志记录器（仅进行关闭操作）
     */
    private void unregisterLogger0(Logger logger) {
        logger.getAppenders().forEach(Appender::close);
    }
}
