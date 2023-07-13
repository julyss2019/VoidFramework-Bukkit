package com.github.julyss2019.bukkit.voidframework.logging.logger;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.appender.Appender;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class Logger {
    private final VoidFrameworkPlugin plugin;
    private final Plugin holder;
    private final List<Appender> appenders = new ArrayList<>();

    public Logger(@NonNull VoidFrameworkPlugin plugin, @NonNull Plugin holder) {
        this.plugin = plugin;
        this.holder = holder;
    }

    /**
     * 添加追加器
     *
     * @param appender 追加器
     */
    public void addAppender(@NonNull Appender appender) {
        appenders.add(appender);
    }

    /**
     * 获取所有追加器
     *
     */
    public List<Appender> getAppenders() {
        return Collections.unmodifiableList(appenders);
    }

    /**
     * 删除追加器
     *
     * @param appender 追加器
     */
    public void removeAppender(@NonNull Appender appender) {
        appenders.remove(appender);
    }

    /**
     * 记录 ERROR 级别的日志
     *
     * @param msg  消息
     * @param args 参数
     */
    public void error(@NonNull String msg, @NonNull Throwable throwable, Object... args) {
        Validator.checkNotContainsNullElement(args, "args cannot contains null");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        throwable.printStackTrace(printWriter);
        log0(Level.ERROR, stringWriter.toString());
    }

    /**
     * 记录 DEBUG 级别的日志
     *
     * @param msg  消息
     * @param args 参数
     */
    public void debug(@NonNull String msg, Object... args) {
        Validator.checkNotContainsNullElement(args, "args cannot contains null");

        log(Level.DEBUG, msg, args);
    }

    /**
     * 记录 WARN 级别的日志
     *
     * @param msg  消息
     * @param args 参数
     */
    public void warn(@NonNull String msg, Object... args) {
        Validator.checkNotContainsNullElement(args, "args cannot contains null");

        log(Level.WARN, msg, args);
    }

    /**
     * 记录 INFO 级别的日志
     *
     * @param msg  消息
     * @param args 参数
     */
    public void info(@NonNull String msg, Object... args) {
        Validator.checkNotContainsNullElement(args, "args cannot contains null");

        log(Level.INFO, msg, args);
    }

    /**
     * 记录日志
     *
     * @param level 等级
     * @param msg   消息
     * @param args  参数
     */
    public void log(@NonNull Level level, @NonNull String msg, Object... args) {
        Validator.checkNotContainsNullElement(args, "args cannot contains null");

        log0(level, String.format(msg, args));
    }

    private void log0(Level level, String msg) {
        for (Appender appender : appenders) {
            if (level.getIntLevel() >= appender.getLevel().getIntLevel()) {
                appender.append(new MessageContext(plugin, holder, level, msg));
            }
        }
    }

    /**
     * 获取插件
     *
     */
    public Plugin getHolder() {
        return holder;
    }
}
