package com.void01.bukkit.voidframework.api.common.library.relocation;

import lombok.NonNull;

/**
 * 主要为了解决 Relocation 重定向规则被 ShadowJar 或 maven-shade-plugin 误 relocate 的问题
 * ShadowJar: relocate('kotlin', 'kotlin123')
 * 代码 shadow 之前: new Relocation("kotlin", "kotlin1910")
 * 代码 shadow 之后: new Relocation("kotlin123", "kotlin1910")
 */
public class SafeRelocation extends Relocation {
    public SafeRelocation(@NonNull String from, @NonNull String to) {
        super(from, to);
    }

    /**
     * 掐头去尾
     */
    public static Relocation createSafely(@NonNull String sourcePattern, @NonNull String destPattern) {
        if (sourcePattern.length() <= 2) {
            throw new RuntimeException("Illegal before pattern");
        }

        if (destPattern.length() <= 2) {
            throw new RuntimeException("Illegal dest pattern");
        }

        return new Relocation(sourcePattern.substring(1, sourcePattern.length() - 1), destPattern.substring(1, destPattern.length() - 1));
    }
}
