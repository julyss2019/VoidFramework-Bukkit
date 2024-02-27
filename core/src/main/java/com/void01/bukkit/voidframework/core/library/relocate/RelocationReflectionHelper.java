package com.void01.bukkit.voidframework.core.library.relocate;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collection;

public class RelocationReflectionHelper {
    private final Class<?> relocationClass;

    public RelocationReflectionHelper(@NonNull Class<?> relocationClass) {
        this.relocationClass = relocationClass;
    }

    @SneakyThrows
    public Object newInstance(@NonNull String s1, @NonNull String s2) {
        return relocationClass.getConstructor(String.class, String.class).newInstance(s1, s2);
    }
}
