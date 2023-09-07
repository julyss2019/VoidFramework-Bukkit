package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter.PatternConverter;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.formatter.PatternFormatter;
import lombok.NonNull;

public class Pattern {
    private final PatternFormatter patternFormatter;
    private final PatternConverter patternConverter;

    public Pattern(@Nullable PatternFormatter patternFormatter, @NonNull PatternConverter patternConverter) {
        this.patternFormatter = patternFormatter;
        this.patternConverter = patternConverter;
    }

    public PatternFormatter getPatternFormatter() {
        return patternFormatter;
    }

    public PatternConverter getPatternConverter() {
        return patternConverter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pattern{");
        sb.append("patternFormatter=").append(patternFormatter);
        sb.append(", patternConverter=").append(patternConverter);
        sb.append('}');
        return sb.toString();
    }
}
