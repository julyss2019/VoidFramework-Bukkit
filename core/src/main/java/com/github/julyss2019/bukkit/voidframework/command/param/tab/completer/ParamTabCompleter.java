package com.github.julyss2019.bukkit.voidframework.command.param.tab.completer;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ParamTabCompleter {
    List<String> complete(CommandSender sender, Class<?> paramType);

    Class<?>[] getSupportedParamTypes();
}
