package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

import lombok.NonNull;

public class FixedUserInputMethodParam extends BaseUserInputMethodParam {
    public FixedUserInputMethodParam(@NonNull Class<?> type, @NonNull String description) {
        super(type, description);
    }
}
