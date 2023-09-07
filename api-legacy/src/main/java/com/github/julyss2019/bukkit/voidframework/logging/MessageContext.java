package com.github.julyss2019.bukkit.voidframework.logging;


import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/**
 * 命令上下文
 */
public class MessageContext {
    private final LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin;
    private final Plugin holder;
    private final Level level;
    private final String message;

    public MessageContext(@NonNull LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin, @NonNull Plugin holder, @NonNull Level level, @NonNull String message) {
        this.legacyVoidFrameworkPlugin = legacyVoidFrameworkPlugin;
        this.holder = holder;
        this.level = level;
        this.message = message;
    }

    /**
     * VoidFramework 插件实例
     */
    public LegacyVoidFrameworkPlugin getVoidFrameworkPlugin() {
        return legacyVoidFrameworkPlugin;
    }

    /**
     * 日志器持有者
     */
    public Plugin getHolder() {
        return holder;
    }

    /**
     * 日志等级
     */
    public Level getLevel() {
        return level;
    }

    /**
     * 日志消息
     */
    public String getMessage() {
        return message;
    }
}
