package com.github.julyss2019.bukkit.voidframework.command.param.validator;

import com.github.julyss2019.bukkit.voidframework.command.annotation.validation.Min;
import lombok.NonNull;

public class MinParamValidator extends BaseParamValidator<Min> {
    public MinParamValidator() {
        super(Min.class);
    }

    @Override
    public boolean verify(@NonNull Object obj, @NonNull Min annotation) {
        return (double) obj >= annotation.value();
    }
}
