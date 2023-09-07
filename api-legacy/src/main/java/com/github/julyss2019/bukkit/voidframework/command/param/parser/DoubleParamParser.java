package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.command.CommandSender;

public class DoubleParamParser extends BaseParamParser {
    public DoubleParamParser() {
        super(new Class[]{double.class, Double.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        double tmp;

        try {
            tmp = Double.parseDouble(param);
        } catch (NumberFormatException e) {
            return Response.failure("不是有效的小数");
        }

        return Response.success(tmp);
    }
}
