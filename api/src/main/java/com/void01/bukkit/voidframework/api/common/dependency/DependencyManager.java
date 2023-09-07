package com.void01.bukkit.voidframework.api.common.dependency;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public interface DependencyManager {
    void loadDependency(@NonNull Plugin plugin, @NonNull Dependency dependency, @NonNull List<Repository> repositories, @NonNull List<Relocation> relocations, boolean recursively);

    default void loadDependency(@NonNull Plugin plugin, @NonNull Dependency dependency, @NonNull List<Relocation> relocations) {
        loadDependency(plugin, dependency, Arrays.asList(Repository.ALIYUN_REPOSITORY, Repository.CENTRAL_REPOSITORY), relocations, true);
    }
}
