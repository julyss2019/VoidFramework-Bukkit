package com.github.julyss2019.bukkit.voidframework.command.internal.param.context;

import lombok.NonNull;

public class SenderSenderParam implements SenderParam {
    private final Class<?> type;

    public SenderSenderParam(@NonNull Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
