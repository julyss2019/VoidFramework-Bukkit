package com.void01.bukkit.voidframework.core.library;

import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import com.void01.bukkit.voidframework.core.library.util.FileUtils;
import lombok.NonNull;
import me.lucko.jarrelocator.JarRelocator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DependencyRelocator {
    public static File relocate(@NonNull File sourceFile, @NonNull List<Relocation> relocations) {
        File newFile = new File(sourceFile.getParent(), FileUtils.getNameWithoutExtension(sourceFile) + "-relocated-" + Math.abs(relocations.hashCode()) + ".jar");

        // 已经有了不再 relocate
        if (newFile.exists()) {
            return newFile;
        }

        JarRelocator relocator = new JarRelocator(sourceFile, newFile, relocations
                .stream()
                .map(relocation -> new me.lucko.jarrelocator.Relocation(relocation.getSourcePattern(), relocation.getDestPattern()))
                .collect(Collectors.toList()));

        try {
            relocator.run();
            return newFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
