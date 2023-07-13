package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import lombok.NonNull;

public abstract class LocaleResourceSupportParamParser extends BaseParamParser {
    protected final LocaleResource localeResource;

    public LocaleResourceSupportParamParser(@NonNull Class<?>[] paramTypes, @NonNull LocaleResource localeResource) {
        super(paramTypes);

        this.localeResource = localeResource;
    }

    public LocaleResource getLocaleResource() {
        return localeResource;
    }
}
