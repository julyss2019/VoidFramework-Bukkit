package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import lombok.NonNull;

@ConverterKeys({"p", "plug", "plugin"})
public class PluginPatternConverter extends BasePatternConverter {
    @Override
    public String convert(@NonNull MessageContext messageContext) {
        return messageContext.getHolder().getName();
    }

    @Override
    public void setParams(@Nullable String params) {
        // ignore
    }
}
