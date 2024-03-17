package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

import lombok.NonNull;

public class ArrayParamDescription extends BaseParamDescription {
    private final Class<?> actualType;

    public ArrayParamDescription(@NonNull Class<?> type, @NonNull String description, @NonNull Class<?> actualType) {
        super(type, description);

        this.actualType = actualType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
