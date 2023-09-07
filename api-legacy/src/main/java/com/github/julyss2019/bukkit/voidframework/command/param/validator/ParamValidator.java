package com.github.julyss2019.bukkit.voidframework.command.param.validator;

import lombok.NonNull;

import java.lang.annotation.Annotation;

public interface ParamValidator<T extends Annotation> {
    Class<T> getAnnotationType();


    boolean verify(@NonNull Object obj, @NonNull T annotation);
}
