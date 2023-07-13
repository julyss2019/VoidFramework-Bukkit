package com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.common.Reflections;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;
import lombok.NonNull;

/**
 * 枚举序列化
 * 提供了枚举读写功能
 * @param <E>
 */
public class EnumSerializable<E extends Enum<E>> implements Serializable<E> {
    private final Class<E> clazz;

    public EnumSerializable(@NonNull Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E get(@NonNull Section parent, @NonNull String path) {
        return Reflections.getEnum(clazz, parent.getString(path));
    }

    @Override
    public void set(@NonNull Section parent, @NonNull String path, @Nullable E value) {
        if (value == null) {
            parent.setByBukkit(path, null);
            return;
        }

        parent.setByBukkit(path, value.name());
    }
}
