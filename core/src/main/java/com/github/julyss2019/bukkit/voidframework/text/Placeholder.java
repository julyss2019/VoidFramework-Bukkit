package com.github.julyss2019.bukkit.voidframework.text;

import lombok.NonNull;

/**
 * 占位符
 */
public class Placeholder {
    private final String key;
    private final String value;

    protected Placeholder(@NonNull String key, @NonNull String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
