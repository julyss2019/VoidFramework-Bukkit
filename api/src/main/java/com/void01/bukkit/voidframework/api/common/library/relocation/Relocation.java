package com.void01.bukkit.voidframework.api.common.library.relocation;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public class Relocation {
    private final String sourcePattern;
    private final String destPattern;

    public Relocation(@NonNull String sourcePattern, @NonNull String destPattern) {
        this.sourcePattern = sourcePattern;
        this.destPattern = destPattern;
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

    public static Relocation createShadowSafely(@NonNull String sourcePattern, @NonNull String destPattern) {
        if (sourcePattern.length() <= 2) {
            throw new RuntimeException("Illegal before pattern");
        }

        if (destPattern.length() <= 2) {
            throw new RuntimeException("Illegal dest pattern");
        }

        return new Relocation(sourcePattern.substring(1, sourcePattern.length() - 1), destPattern.substring(1, destPattern.length() - 1));
    }
}
