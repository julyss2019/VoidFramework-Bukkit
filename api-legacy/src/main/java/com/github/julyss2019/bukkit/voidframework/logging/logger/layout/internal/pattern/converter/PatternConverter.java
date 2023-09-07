package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;

import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import lombok.NonNull;

public interface PatternConverter {
    String convert(@NonNull MessageContext messageContext);

    void setParams(String params);
}
