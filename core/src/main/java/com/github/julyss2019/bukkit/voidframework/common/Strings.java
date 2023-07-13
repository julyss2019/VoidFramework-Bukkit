package com.github.julyss2019.bukkit.voidframework.common;

import lombok.NonNull;
import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;

public class Strings {
    public static boolean isNullOrEmpty(@Nullable String str) {
        return str != null && str.isEmpty();
    }

    public static boolean isDouble(@Nullable String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInteger(@NonNull String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
