package com.void01.bukkit.voidframework.core.library.relocate;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collection;

public class JarRelocatorReflectionHelper {
    private final Class<?> relocatorClass;

    public JarRelocatorReflectionHelper(@NonNull Class<?> relocatorClass) {
        this.relocatorClass = relocatorClass;
    }

    @SneakyThrows
    public Object newInstance(@NonNull File file1, @NonNull File file2, @NonNull Collection<?> collection) {
        return relocatorClass.getConstructor(File.class, File.class, Collection.class).newInstance(file1, file2, collection);
    }

    @SneakyThrows
    public void run(@NonNull Object obj) {
        relocatorClass.getDeclaredMethod("run").invoke(obj);
    }
}
