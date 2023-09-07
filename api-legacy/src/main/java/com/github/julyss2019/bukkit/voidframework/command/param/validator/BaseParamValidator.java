package com.github.julyss2019.bukkit.voidframework.command.param.validator;

import com.github.julyss2019.bukkit.voidframework.command.param.validator.ParamValidator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public abstract class BaseParamValidator<T extends Annotation> implements ParamValidator<T> {
    @Getter
    @NonNull
    private final Class<T> annotationType;
}
