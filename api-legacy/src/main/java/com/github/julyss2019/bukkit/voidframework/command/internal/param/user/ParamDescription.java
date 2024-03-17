package com.github.julyss2019.bukkit.voidframework.command.internal.param.user;

/**
 * 输入参数
 */
public interface ParamDescription {
    Class<?> getType();

    String getDescription();
}