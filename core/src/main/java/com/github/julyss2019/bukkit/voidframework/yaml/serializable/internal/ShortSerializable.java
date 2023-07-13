package com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;
import lombok.NonNull;

public class ShortSerializable implements Serializable<Short> {
    private static ShortSerializable instance;

    public static ShortSerializable getInstance() {
        if (instance == null) {
            instance = new ShortSerializable();
        }

        return instance;
    }

    @Override
    public Short get(@NonNull Section parent, @NonNull String path) {
        return (short) parent.getInt(path, null);
    }

    @Override
    public void set(@NonNull Section parent, @NonNull String path, @Nullable Short value) {
        parent.setByBukkit(path, (int) value);
    }
}
