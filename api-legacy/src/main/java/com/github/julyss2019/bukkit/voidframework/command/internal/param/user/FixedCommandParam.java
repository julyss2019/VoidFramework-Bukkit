package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

import lombok.NonNull;

public class FixedCommandParam extends BaseCommandParam {
    public FixedCommandParam(@NonNull Class<?> type, @NonNull String description) {
        super(type, description);
    }
}
