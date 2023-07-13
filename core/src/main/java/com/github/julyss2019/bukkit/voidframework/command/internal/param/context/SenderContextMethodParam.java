package com.github.julyss2019.bukkit.voidframework.command.internal.param.context;

import lombok.NonNull;

public class SenderContextMethodParam implements ContextMethodParam {
    private final Class<?> type;

    public SenderContextMethodParam(@NonNull Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
