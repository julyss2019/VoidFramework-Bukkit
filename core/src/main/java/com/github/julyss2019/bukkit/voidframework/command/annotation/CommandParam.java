package com.github.julyss2019.bukkit.voidframework.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令参数注解
 * 1. 被该注解修饰的参数会通过 {@link com.github.julyss2019.bukkit.voidframework.command.CommandManager} 自动将文本参数解析为目标参数
 * 2. 提供了介绍功能
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParam {
    /**
     * 命令介绍
     */
    String description();

    /**
     * 是否为可选的参数
     * 可选参数必须置后, 正确性距离:
     * /test give [a] <b> 错误
     * /test give <a> [b] 正确
     */
    boolean optional() default false;
}
