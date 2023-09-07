package com.github.julyss2019.bukkit.voidframework.command.param.tab.completer;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;

import java.util.Arrays;


public abstract class BaseParamTabCompleter implements ParamTabCompleter {
    private final Class<?>[] paramTypes;

    public BaseParamTabCompleter(@NonNull Class<?>[] paramTypes) {
        Validator.checkNotContainsNullElement(paramTypes);

        this.paramTypes = Arrays.copyOf(paramTypes, paramTypes.length);
    }

    @Override
    public Class<?>[] getSupportedParamTypes() {
        return Arrays.copyOf(paramTypes, paramTypes.length);
    }
}
