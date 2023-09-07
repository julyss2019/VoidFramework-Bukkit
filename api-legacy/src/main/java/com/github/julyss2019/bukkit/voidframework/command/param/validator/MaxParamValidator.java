package com.github.julyss2019.bukkit.voidframework.command.param.validator;

import com.github.julyss2019.bukkit.voidframework.command.annotation.validation.Max;
import lombok.NonNull;

public class MaxParamValidator extends BaseParamValidator<Max> {
    public MaxParamValidator() {
        super(Max.class);
    }

    @Override
    public boolean verify(@NonNull Object obj, @NonNull Max annotation) {
        return (double) obj <= annotation.value();
    }
}
