package com.void01.bukkit.voidframework.api.common.library;

import com.void01.bukkit.voidframework.api.common.VoidFramework3;
import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DependencyLoader {
    private final LibraryManager libraryManager;
    private final List<Repository> repositories = new ArrayList<>();

    public DependencyLoader(@NonNull LibraryManager libraryManager) {
        this.libraryManager = libraryManager;

        repositories.add(Repository.ALIYUN);
        repositories.add(Repository.CENTRAL);
    }

    public DependencyLoader() {
        this(VoidFramework3.getLibraryManager());
    }

    public void setRepositories(@NonNull List<Repository> repositories) {
        if (!repositories.contains(null)) {
            throw new IllegalArgumentException("Parameter repositories contains null");
        }

        this.repositories.clear();
        this.repositories.addAll(repositories);
    }

    public List<Repository> getRepositories() {
        return Collections.unmodifiableList(repositories);
    }

    public void load(@NonNull String gradleStyleExpression, Relocation... relocations) {
        load(gradleStyleExpression, VoidFramework3.class.getClassLoader(), relocations);
    }

    public void load(@NonNull String gradleStyleExpression, @NonNull ClassLoader classLoader, Relocation... relocations) {
        load(Dependency.fromGradleStyleExpression(gradleStyleExpression), classLoader, Arrays.asList(relocations));
    }

    public void load(@NonNull Dependency dependency, @NonNull ClassLoader classLoader, @NonNull List<Relocation> relocations) {
        libraryManager.loadDependency(dependency, classLoader, repositories, relocations, false);
    }

    public void loadRecursively(@NonNull String gradleStyleExpression, Relocation... relocations) {
        loadRecursively(gradleStyleExpression, VoidFramework3.class.getClassLoader(), relocations);
    }

    public void loadRecursively(@NonNull String gradleStyleExpression, @NonNull ClassLoader classLoader, Relocation... relocations) {
        loadRecursively(Dependency.fromGradleStyleExpression(gradleStyleExpression), classLoader, Arrays.asList(relocations));
    }

    public void loadRecursively(@NonNull Dependency dependency, @NonNull ClassLoader classLoader, @NonNull List<Relocation> relocations) {
        libraryManager.loadDependency(dependency, classLoader, repositories, relocations, true);
    }
}
