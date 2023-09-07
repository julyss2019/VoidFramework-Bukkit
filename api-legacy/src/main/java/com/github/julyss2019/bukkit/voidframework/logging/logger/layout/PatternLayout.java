package com.github.julyss2019.bukkit.voidframework.logging.logger.layout;

import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.Pattern;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.PatternParser;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.formatter.PatternFormatter;
import lombok.NonNull;

import java.util.List;

public class PatternLayout implements Layout {
    private final String patternText;
    private final List<Pattern> patterns;

    public PatternLayout(@NonNull String patternText) {
        this.patternText = patternText;
        this.patterns = PatternParser.parse(patternText);
    }

    public String getPatternText() {
        return patternText;
    }

    @Override
    public String format(MessageContext context) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Pattern pattern : patterns) {
            PatternFormatter formatter = pattern.getPatternFormatter();
            String converted = pattern.getPatternConverter().convert(context);

            if (formatter != null) {
                stringBuilder.append(formatter.format(converted));
            } else {
                stringBuilder.append(converted);
            }
        }

        return stringBuilder.toString();
    }
}
