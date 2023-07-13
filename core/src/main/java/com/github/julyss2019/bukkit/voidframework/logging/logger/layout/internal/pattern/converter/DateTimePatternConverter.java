package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ConverterKeys({"d", "date"})
public class DateTimePatternConverter extends BasePatternConverter {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setParams(@Nullable String params) {
        if (params == null) {
            return;
        }

        this.dateTimeFormatter = DateTimeFormatter.ofPattern(params);
    }

    @Override
    public String convert(@NonNull MessageContext messageContext) {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
