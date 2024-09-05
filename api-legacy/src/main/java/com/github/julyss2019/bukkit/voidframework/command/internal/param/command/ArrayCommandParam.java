package com.github.julyss2019.bukkit.voidframework.command.internal.param.command;

import lombok.NonNull;

public class ArrayCommandParam extends BaseCommandParam {
    private final Class<?> actualType;

    public ArrayCommandParam(@NonNull Class<?> type, @NonNull String description, @NonNull Class<?> actualType) {
        super(type, description);

        this.actualType = actualType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
