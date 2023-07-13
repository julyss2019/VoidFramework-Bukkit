package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConverterKeys {
    String[] value();
}
