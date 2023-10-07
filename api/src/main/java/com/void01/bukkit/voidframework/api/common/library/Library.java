package com.void01.bukkit.voidframework.api.common.library;

import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import com.void01.bukkit.voidframework.api.common.library.relocation.SafeRelocation;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 库包含一个依赖，而这个依赖又可能产生 N 个依赖，所以 Library 里有个 Dependency
 */
@Getter
public class Library {
    private final Dependency dependency;
    private final List<Repository> repositories;
    private final List<Relocation> relocations;
    private final URLClassLoader classLoader;
    private final boolean resolveRecursively;

    private Library(@NonNull Dependency dependency, @NonNull List<Repository> repositories,
                    @NonNull List<Relocation> relocations, @NonNull URLClassLoader classLoader, boolean resolveRecursively) {
        if (repositories.contains(null)) {
            throw new IllegalArgumentException("repositories cannot contains null");
        }

        if (relocations.contains(null)) {
            throw new IllegalArgumentException("relocations cannot contains null");
        }

        this.dependency = dependency;
        this.repositories = new ArrayList<>(repositories);
        this.relocations = new ArrayList<>(relocations);
        this.classLoader = classLoader;
        this.resolveRecursively = resolveRecursively;
    }

    public static final class Builder {
        private String groupId;
        private String artifactId;
        private String version;
        private URLClassLoader classLoader;
        private List<Repository> repositories = new ArrayList<>();
        private List<Relocation> relocations = new ArrayList<>();
        private boolean resolveRecursively;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        /**
         * 通过 Gradle 风格的表达式设置 groupId, artifactId, version
         */
        public Builder setDependencyByGradleStyleExpression(@NonNull String expression) {
            String[] split = expression.split(":");

            if (split.length != 3) {
                throw new IllegalArgumentException("Invalid expression");
            }

            setGroupId(split[0]);
            setArtifactId(split[1]);
            setVersion(split[2]);
            return this;
        }

        public Builder setGroupId(@NonNull String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder setArtifactId(@NonNull String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder setVersion(@NonNull String version) {
            this.version = version;
            return this;
        }

        public Builder setClassLoader(@NonNull URLClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder setClassLoaderByBukkitPlugin(@NonNull Plugin plugin) {
            setClassLoader((URLClassLoader) plugin.getClass().getClassLoader());
            return this;
        }

        public Builder setRelocations(@NonNull List<Relocation> relocations) {
            if (repositories.contains(null)) {
                throw new IllegalArgumentException("Repositories can not contains null");
            }

            this.relocations = relocations;
            return this;
        }

        public Builder addRelocation(@NonNull Relocation relocation) {
            if (!relocations.contains(relocation)) {
                this.relocations.add(relocation);
            }

            return this;
        }

        public Builder addSafeRelocation(@NonNull String sourcePattern, @NonNull String destPattern) {
            return addRelocation(SafeRelocation.createSafely(sourcePattern, destPattern));
        }

        public Builder setResolveRecursively(boolean resolveRecursively) {
            this.resolveRecursively = resolveRecursively;
            return this;
        }

        public Builder setRepositories(@NonNull List<Repository> repositories) {
            if (repositories.contains(null)) {
                throw new IllegalArgumentException("Repositories can not contains null");
            }

            return this;
        }

        public Builder addRepositories(@NonNull Repository... repositories) {
            List<@NonNull Repository> list = Arrays.asList(repositories);

            if (list.contains(null)) {
                throw new IllegalArgumentException("Repositories can not contains null");
            }

            list.forEach(this::addRepository);
            return this;
        }

        public Builder addRepository(@NonNull Repository repository) {
            if (!repositories.contains(repository)) {
                repositories.add(repository);
            }

            return this;
        }

        public Builder addRepository(@NonNull String repositoryUrl) {
            return addRepository(new Repository(repositoryUrl));
        }

        public Library build() {
            Objects.requireNonNull(groupId, "missing groupId");
            Objects.requireNonNull(groupId, "missing artifactId");
            Objects.requireNonNull(groupId, "missing version");

            return new Library(new Dependency(groupId, artifactId, version), repositories, relocations, classLoader, resolveRecursively);
        }
    }
}
