package com.github.julyss2019.bukkit.voidframework.locale.resource;

import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import lombok.NonNull;

import java.util.Locale;

public abstract class BaseLocaleResource implements LocaleResource {
    protected final Locale locale;

    public BaseLocaleResource(@NonNull Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
