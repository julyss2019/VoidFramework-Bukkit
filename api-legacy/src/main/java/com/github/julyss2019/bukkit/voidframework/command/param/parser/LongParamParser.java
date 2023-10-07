package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public class LongParamParser extends BaseParamParser {
    public LongParamParser() {
        super(new Class[]{long.class, Long.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        long tmp;

        try {
            tmp = Long.parseLong(param);
        } catch (NumberFormatException e) {
           return Response.failure("长整数不合法");
        }

        return Response.success(tmp);
    }
}
