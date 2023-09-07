package com.github.julyss2019.bukkit.voidframework.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 命令映射注解
 * 以 /root item give APPLE 1, /root server shutdown 为例：
 * root 是一级命令映射. item/server 是二级命令映射
 * 当在 {@link org.bukkit.plugin.Plugin} 类使用该注时, 会被认为是一级命令映射
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMapping {
    /**
     * 路径表达式
     * 支持 "/" 分隔
     */
    String value();

    /**
     * 权限
     */
    String permission() default "";
}
