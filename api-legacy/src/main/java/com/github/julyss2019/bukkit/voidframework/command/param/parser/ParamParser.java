package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public interface ParamParser {
    Response parse(CommandSender sender, Class<?> paramType, String param);

    Class<?>[] getSupportedParamTypes();
}
