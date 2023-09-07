package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public class BooleanParamParser extends BaseParamParser {
    public BooleanParamParser() {
        super(new Class[]{boolean.class, Boolean.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        boolean tmp;

        try {
            tmp = Boolean.parseBoolean(param);
        } catch (NumberFormatException e) {
            return Response.failure("布尔值不合法");
        }

        return Response.success(tmp);
    }
}
