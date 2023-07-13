package com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.common.Reflections;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import lombok.NonNull;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;

import java.util.Collections;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * 枚举列表适配器
 * @param <E>
 */
public class EnumSetSerializable<E extends Enum<E>> implements Serializable<EnumSet<E>> {
    private final Class<E> clazz;

    public EnumSetSerializable(@NonNull Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public EnumSet<E> get(@NonNull Section parent, @NonNull String path) {
        EnumSet<E> enumSet = EnumSet.noneOf(clazz);

        parent.getStringList(path)
                .forEach(strEnum -> enumSet.add(Reflections.getEnum(clazz, strEnum)));
        return enumSet;
    }

    @Override
    public void set(@NonNull Section parent, @NonNull String path, @Nullable EnumSet<E> value) {
        if (value == null) {
            parent.setByBukkit(path, Collections.emptyList());
            return;
        }

        parent.setByBukkit(path, value.stream().map(Enum::name).distinct().collect(Collectors.toList()));
    }
}
