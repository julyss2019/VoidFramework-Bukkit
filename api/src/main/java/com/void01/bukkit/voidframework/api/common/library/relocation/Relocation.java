package com.void01.bukkit.voidframework.api.common.library.relocation;

import lombok.NonNull;

import java.util.Objects;

public class Relocation {
    private final String sourcePattern;
    private final String destPattern;

    public Relocation(@NonNull String sourcePattern, @NonNull String destPattern) {
        this.sourcePattern = sourcePattern;
        this.destPattern = destPattern;
    }

    public String getSourcePattern() {
        return sourcePattern;
    }

    public String getDestPattern() {
        return destPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relocation that = (Relocation) o;
        return Objects.equals(sourcePattern, that.sourcePattern) && Objects.equals(destPattern, that.destPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourcePattern, destPattern);
    }
}
