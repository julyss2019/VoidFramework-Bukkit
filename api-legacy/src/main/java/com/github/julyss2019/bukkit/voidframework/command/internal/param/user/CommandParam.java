package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

/**
 * 输入参数
 */
public interface CommandParam {
    Class<?> getType();

    String getDescription();
}