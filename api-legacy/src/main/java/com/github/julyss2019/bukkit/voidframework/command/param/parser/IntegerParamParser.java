package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public class IntegerParamParser extends BaseParamParser {
    public IntegerParamParser() {
        super(new Class[]{int.class, Integer.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        int tmp;

        try {
            tmp = Integer.parseInt(param);
        } catch (NumberFormatException e) {
           return Response.failure("整数不合法");
        }

        return Response.success(tmp);
    }
}
