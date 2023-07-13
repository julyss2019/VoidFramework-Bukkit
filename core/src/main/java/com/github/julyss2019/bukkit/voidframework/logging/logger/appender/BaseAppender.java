package com.github.julyss2019.bukkit.voidframework.logging.logger.appender;

import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.Layout;
import lombok.NonNull;

public abstract class BaseAppender implements Appender {
    private Layout layout;
    private Level threshold;
    private boolean closed;

    public BaseAppender(@NonNull Layout layout, @NonNull Level threshold) {
        setLayout(layout);
        setLevel(threshold);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void setLevel(@NonNull Level level) {
        this.threshold = level;
    }

    @Override
    public Level getLevel() {
        return threshold;
    }

    @Override
    public void setLayout(@NonNull Layout layout) {
        this.layout = layout;
    }

    @Override
    public Layout getLayout() {
        return layout;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public void append(@NonNull MessageContext messageContext) {
        if (isClosed()) {
            throw new RuntimeException("appender already closed");
        }
    }
}
