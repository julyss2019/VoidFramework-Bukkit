package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import lombok.NonNull;

@ConverterKeys({"l", "lv", "level"})
public class LevelPatternConverter extends BasePatternConverter {
    @Override
    public String convert(@NonNull MessageContext messageContext) {
        return messageContext.getLevel().name();
    }

    @Override
    public void setParams(@Nullable String params) {
        // ignore
    }
}
