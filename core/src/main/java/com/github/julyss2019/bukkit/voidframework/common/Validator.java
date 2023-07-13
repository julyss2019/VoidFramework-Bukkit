package com.github.julyss2019.bukkit.voidframework.common;


import lombok.NonNull;
import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class Validator {
    public static void checkNotContainsNullElement(@NonNull Object[] array) {
        checkNotContainsNullElement(array, null);
    }

    public static void checkNotContainsNullElement(@NonNull Object[] array, @Nullable String msg) {
        for (Object obj : array) {
            //noinspection ConstantValue
            checkState(obj != null, msg == null ? "array cannot contains null element" : msg);
        }
    }

    public static <T> void checkNotContainsNullElement(@NonNull Collection<T> collection) {
        checkNotContainsNullElement(collection, null);
    }

    public static <T> void checkNotContainsNullElement(@NonNull Collection<T> collection, @Nullable String msg) {
        checkState(!collection.contains(null), msg == null ? "collection cannot contains null element" : msg);
    }

    public static void checkValidItem(ItemStack item) {
        checkState(Items.isValid(item), "item is not a valid item");
    }

    public static void checkState(boolean b, @Nullable String msg) {
        if (!b) {
            if (msg != null) {
                throw new IllegalArgumentException(msg);
            }

            throw new IllegalArgumentException();
        }
    }

    public static <T> void checkNotEmpty(@NonNull Collection<T> collection, @Nullable String msg) {
        checkState(collection.isEmpty(), "collection must not be empty");
    }
}
