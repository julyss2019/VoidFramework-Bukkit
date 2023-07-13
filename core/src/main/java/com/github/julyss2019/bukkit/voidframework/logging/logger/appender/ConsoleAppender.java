package com.github.julyss2019.bukkit.voidframework.logging.logger.appender;

import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.Layout;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制台追加器
 */
public class ConsoleAppender extends BaseAppender {
    public final Map<Level, ChatColor> colorMap = new HashMap<>();

    public ConsoleAppender(@NonNull Layout layout, @NonNull Level threshold) {
        super(layout, threshold);

        setDefaultColors();
    }

    private void setDefaultColors() {
        setColor(Level.DEBUG, ChatColor.DARK_PURPLE);
        setColor(Level.INFO, ChatColor.WHITE);
        setColor(Level.WARN, ChatColor.YELLOW);
        setColor(Level.ERROR, ChatColor.RED);
    }

    public void setColor(@NonNull Level level, @NonNull ChatColor color) {
        colorMap.put(level, color);
    }

    public ChatColor getColor(@NonNull Level level) {
        return colorMap.get(level);
    }

    @Override
    public void append(MessageContext messageContext) {
        super.append(messageContext);

        ChatColor color = getColor(messageContext.getLevel());
        String message = color + getLayout().format(messageContext);

        // 确保在主线程写入
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getConsoleSender().sendMessage(message);
        } else {
            messageContext.getVoidFrameworkPlugin().getConsoleAppenderFlushTask().addConsoleMessage(message);
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
