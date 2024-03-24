package com.github.julyss2019.bukkit.voidframework.yaml;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;

import java.util.Arrays;

/**
 * 路径描述
 */
public class Paths {
    private final String[] paths;

    private Paths(String[] paths) {
        this.paths = paths;
    }

    public String[] getPaths() {
        return paths;
    }

    /**
     * 创建路径表述
     */
    public static Paths of(@NonNull String... paths) {
        Validator.checkNotContainsNullElement(paths, "paths cannot contains null");

        return new Paths(paths);
    }

    @Override
    public String toString() {
        return "Paths{" +
                "paths=" + Arrays.toString(paths) +
                '}';
    }
}
