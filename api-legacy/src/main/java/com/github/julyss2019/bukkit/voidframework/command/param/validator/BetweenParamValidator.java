package com.github.julyss2019.bukkit.voidframework.command.param.validator;

import com.github.julyss2019.bukkit.voidframework.command.annotation.validation.Between;
import lombok.NonNull;

public class BetweenParamValidator extends BaseParamValidator<Between> {
    public BetweenParamValidator() {
        super(Between.class);
    }

    @Override
    public boolean verify(@NonNull Object obj, @NonNull Between annotation) {
        double tmp = (double) obj;

        return tmp >= annotation.min() && tmp <= annotation.max();
    }
}
