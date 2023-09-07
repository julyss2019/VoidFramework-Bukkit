package com.github.julyss2019.bukkit.voidframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 仅标注用
 */
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
public @interface Nullable {
}
