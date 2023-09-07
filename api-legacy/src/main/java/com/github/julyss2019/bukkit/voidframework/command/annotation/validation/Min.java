package com.github.julyss2019.bukkit.voidframework.command.annotation.validation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 最小值验证注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SupportType({int.class, float.class, double.class, Integer.class, Float.class, Double.class})
public @interface Min {
    double value();
}
