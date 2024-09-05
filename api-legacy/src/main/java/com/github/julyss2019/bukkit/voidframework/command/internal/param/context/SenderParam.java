package com.github.julyss2019.bukkit.voidframework.command.internal.param.context;

import lombok.NonNull;

public class SenderParam implements ActiveContextParam {
    private final Class<?> type;

    public SenderParam(@NonNull Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
