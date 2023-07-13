package com.github.julyss2019.bukkit.voidframework.yaml.serializable;


import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import lombok.NonNull;

public interface Serializable<T> {
    T get(@NonNull Section parent, @NonNull String path);

    void set(@NonNull Section parent, @NonNull String path, @Nullable T value);
}
