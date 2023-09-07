package com.github.julyss2019.bukkit.voidframework.locale.resource;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import com.github.julyss2019.bukkit.voidframework.text.Texts;
import lombok.NonNull;

import java.util.List;
import java.util.Locale;

public interface LocaleResource {
    LocaleResource getLocalResource(@NonNull String key);

    default List<String> getStringList(@NonNull String key) {
        return getStringList(key, null);
    }

    List<String> getStringList(@NonNull String key, @Nullable List<String> def);

    default String getString(@NonNull String key) {
        return getString(key, null);
    }

    String getString(@NonNull String key, @Nullable String def);

    Locale getLocale();

    void reload();
}
