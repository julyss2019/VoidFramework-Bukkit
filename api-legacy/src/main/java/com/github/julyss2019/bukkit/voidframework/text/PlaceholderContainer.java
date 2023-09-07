package com.github.julyss2019.bukkit.voidframework.text;

import lombok.NonNull;

import java.util.*;

/**
 * 占位符容器
 */
public class PlaceholderContainer  {
    private final Map<String, Object> map = new HashMap<>();

    /**
     * 放置占位符
     */
    public PlaceholderContainer put(@NonNull String key, @NonNull Object value) {
        map.put(key, value);
        return this;
    }

    /**
     * 放置占位符
     */
    public PlaceholderContainer put(@NonNull String key, @NonNull String value) {
        map.put(key, value);
        return this;
    }

    public Object getValue(@NonNull String key) {
        return map.get(key);
    }
}
