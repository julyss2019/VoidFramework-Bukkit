package com.void01.bukkit.voidframework.api.common.dependency;

import lombok.NonNull;

public class Dependency {
    @NonNull
    private final String groupId;
    @NonNull
    private final String artifactId;
    @NonNull
    private final String version;

    public Dependency(@NonNull String groupId, @NonNull String artifactId, @NonNull String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static Dependency createByGradleExpression(@NonNull String exp) {
        String[] split = exp.split(":");

        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return new Dependency(split[0], split[1], split[2]);
    }

    public String getAsGradleStyleExpression() {
        return groupId + ":" + artifactId + ":" + version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }
}
