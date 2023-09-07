package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public class StringParamParser extends BaseParamParser {
    public StringParamParser() {
        super(new Class[]{String.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        return Response.success(param);
    }
}
