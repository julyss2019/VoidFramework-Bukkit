package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

import lombok.NonNull;

public class BaseUserInputMethodParam implements UserInputMethodParam {
    private final Class<?> type;
    private final String description;

    public BaseUserInputMethodParam(@NonNull Class<?> type, @NonNull String description) {
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
