package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;


public abstract class BaseParamParser implements ParamParser {
    private final Class<?>[] paramTypes;

    public BaseParamParser(@NonNull Class<?>[] paramTypes) {
        Validator.checkNotContainsNullElement(paramTypes);

        this.paramTypes = paramTypes;
    }

    @Override
    public Class<?>[] getSupportedParamTypes() {
        return paramTypes;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }
}
