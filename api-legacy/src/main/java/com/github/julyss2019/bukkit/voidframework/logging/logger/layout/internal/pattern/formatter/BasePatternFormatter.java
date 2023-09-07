package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.formatter;

import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter.PatternConverter;
import lombok.NonNull;

public abstract class BasePatternFormatter implements PatternFormatter {
    protected final PatternConverter patternConverter;

    public BasePatternFormatter(@NonNull PatternConverter patternConverter) {
        this.patternConverter = patternConverter;
    }
}
