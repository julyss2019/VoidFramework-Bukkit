package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

import lombok.NonNull;

public class FixedParamDescription extends BaseParamDescription {
    public FixedParamDescription(@NonNull Class<?> type, @NonNull String description) {
        super(type, description);
    }
}
