package com.github.julyss2019.bukkit.voidframework.yaml;


import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;

/**
 * Section 读取时的默认值
 * @param <T> 默认值类型
 */
public class DefaultValue<T> {
    private final T value;

    private DefaultValue(@Nullable T value) {
        this.value = value;
    }

    /**
     * 获取默认值
     */
    public T getValue() {
        return this.value;
    }

    /**
     * 创建默认值
     */
    public static <T> DefaultValue<T> of(@Nullable T value) {
        return new DefaultValue<>(value);
    }
}
