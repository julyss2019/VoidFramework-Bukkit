package com.github.julyss2019.bukkit.voidframework.command.annotation;



import com.github.julyss2019.bukkit.voidframework.command.SenderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令体注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandBody {
    /**
     * 命令 id
     */
    String name() default "";

    /**
     * 命令 id
     */
    String value() default "";

    /**
     * 命令描述
     */
    String description();

    /**
     * 权限
     * @return 默认无权限
     */
    String permission() default "";

    /**
     * 发送者
     * @return 默认允许包含 PLAYER 和 CONSOLE
     */
    SenderType[] senders() default {SenderType.PLAYER, SenderType.CONSOLE};
}
