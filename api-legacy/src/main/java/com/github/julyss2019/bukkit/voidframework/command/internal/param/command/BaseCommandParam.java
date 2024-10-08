package com.github.julyss2019.bukkit.voidframework.command.internal.param.command;

import lombok.NonNull;

public class BaseCommandParam implements ActiveCommandParam {
    private final Class<?> type;
    private final String description;

    public BaseCommandParam(@NonNull Class<?> type, @NonNull String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
