package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;

import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;
import lombok.NonNull;

public class LiteralPatternConverter extends BasePatternConverter {
    private final String literal;

    public LiteralPatternConverter(String literal) {
        this.literal = literal;
    }

    @Override
    public String convert(@NonNull MessageContext messageContext) {
        return literal;
    }

    @Override
    public void setParams(@NonNull String params) {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LiteralPatternConverter{");
        sb.append("literal='").append(literal).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
