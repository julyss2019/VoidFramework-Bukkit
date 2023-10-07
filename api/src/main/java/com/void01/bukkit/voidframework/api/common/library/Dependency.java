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
}
