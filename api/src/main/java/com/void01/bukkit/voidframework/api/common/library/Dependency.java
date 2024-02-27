package com.void01.bukkit.voidframework.api.common.library;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Dependency {
    @NonNull
    private final String groupId;
    @NonNull
    private final String artifactId;
    @NonNull
    private final String version;

    public String getAsGradleStyleExpression() {
        return groupId + ":" + artifactId + ":" + version;
    }

    public static Dependency fromGradleStyleExpression(@NonNull String expression) {
        String[] split = expression.split(":");

        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return new Dependency(split[0], split[1], split[2]);
    }
}
