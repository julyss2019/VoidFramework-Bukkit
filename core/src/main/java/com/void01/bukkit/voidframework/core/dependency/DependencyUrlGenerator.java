package com.void01.bukkit.voidframework.core.dependency;

import com.void01.bukkit.voidframework.api.common.dependency.Dependency;
import com.void01.bukkit.voidframework.api.common.dependency.Repository;
import lombok.NonNull;

class DependencyUrlGenerator {
    public static String generateMainFileUrl(@NonNull Dependency dependency, @NonNull Repository repository, @NonNull DependencyFileType type) {
        return getBaseUrl(dependency, repository) + "/" + getBaseFileName(dependency) + "." + type.name().toLowerCase();
    }

    public static String generateMd5FileUrl(@NonNull Dependency dependency, @NonNull Repository repository, @NonNull DependencyFileType type) {
        return generateMainFileUrl(dependency, repository, type) + ".md5";
    }

    public static String getBaseFileName(@NonNull Dependency dependency) {
        return dependency.getArtifactId() + "-" + dependency.getVersion();
    }

    public static String getBaseUrl(@NonNull Dependency dependency, @NonNull Repository repository) {
        return repository.getUrl() + "/" + dependency.getGroupId().replace(".", "/") + "/" + dependency.getArtifactId() + "/" + dependency.getVersion();
    }
}
