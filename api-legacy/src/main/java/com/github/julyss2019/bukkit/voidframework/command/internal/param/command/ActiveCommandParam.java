package com.github.julyss2019.bukkit.voidframework.command.internal.param.command;

import com.github.julyss2019.bukkit.voidframework.command.internal.param.ActiveParam;

/**
 * 输入参数
 */
public interface ActiveCommandParam extends ActiveParam {
    Class<?> getType();

    String getDescription();
}