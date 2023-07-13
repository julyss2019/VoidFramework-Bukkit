package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.formatter;

import lombok.NonNull;

public class LeftAlignedPatternFormatter implements PatternFormatter {
    private final int width;

    public LeftAlignedPatternFormatter(int width) {
        this.width = width;
    }

    @Override
    public String format(@NonNull String text) {
        return String.format("-" + width, text);
    }
}
