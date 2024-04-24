package com.void01.bukkit.voidframework.core.library;

import com.void01.bukkit.voidframework.api.common.library.Dependency;
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin;
import lombok.NonNull;

import java.io.File;

public class DependencyFileHelper {
    private final File libsDir;

    public DependencyFileHelper(@NonNull VoidFrameworkPlugin plugin) {
        this.libsDir = new File(plugin.getDataFolder(), "repo-libs");
    }

    public File getDependencyMainFile(@NonNull Dependency dependency, @NonNull DependencyFileType type) {
        return new File(getDependencyDir(dependency), getDependencyBaseName(dependency) + "." + type.name().toLowerCase());
    }

    public File getDependencyMd5File(@NonNull Dependency dependency, @NonNull DependencyFileType type) {
        return new File(getDependencyDir(dependency), getDependencyBaseName(dependency) + "." + type.name().toLowerCase() + ".md5");
    }

    private File getDependencyDir(@NonNull Dependency dependency) {
        String subDir = dependency.getGroupId().replace(".", File.separator) +
                File.separator +
                dependency.getArtifactId() +
                File.separator +
                dependency.getVersion();

        return new File(libsDir, subDir);
    }

    private String getDependencyBaseName(@NonNull Dependency dependency) {
        return dependency.getArtifactId() + "-" + dependency.getVersion();
    }
}
