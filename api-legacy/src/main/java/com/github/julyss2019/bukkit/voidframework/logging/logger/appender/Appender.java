package com.github.julyss2019.bukkit.voidframework.logging.logger.appender;

import com.github.julyss2019.bukkit.voidframework.logging.Level;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.Layout;
import lombok.NonNull;

public interface Appender {
    void append(@NonNull MessageContext messageContext);

    void setLevel(@NonNull Level level);

    Level getLevel();

    void setLayout(@NonNull Layout layout);

    Layout getLayout();

    void close();

    boolean isClosed();
}
