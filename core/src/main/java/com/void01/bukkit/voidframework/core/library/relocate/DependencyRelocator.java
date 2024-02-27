package com.void01.bukkit.voidframework.core.library.relocate;

import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import com.void01.bukkit.voidframework.core.library.util.FileUtils;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyRelocator {
    private final JarRelocatorReflectionHelper jarRelocatorReflectionHelper;
    private final RelocationReflectionHelper relocationReflectionHelper;

    public DependencyRelocator(@NonNull Class<?> relocatorClass, @NonNull Class<?> relocationClass) {
        this.jarRelocatorReflectionHelper = new JarRelocatorReflectionHelper(relocatorClass);
        this.relocationReflectionHelper = new RelocationReflectionHelper(relocationClass);
    }

    public File relocate(@NonNull File sourceFile, @NonNull List<Relocation> relocations) {
        File destFile = new File(sourceFile.getParent(), FileUtils.getNameWithoutExtension(sourceFile) + "-relocated-" + Math.abs(relocations.hashCode()) + ".jar");

        // 已经有了不再 relocate
        if (destFile.exists()) {
            return destFile;
        }

        Object jarRelocatorInst = jarRelocatorReflectionHelper.newInstance(sourceFile, destFile, relocations
                .stream()
                .map(relocation -> relocationReflectionHelper.newInstance(relocation.getSourcePattern(), relocation.getDestPattern()))
                .collect(Collectors.toList()));

        jarRelocatorReflectionHelper.run(jarRelocatorInst);
        return destFile;
    }
}
