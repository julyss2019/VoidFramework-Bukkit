package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import com.github.julyss2019.bukkit.voidframework.common.Reflections;
import org.bukkit.command.CommandSender;

public class EnumParamParser extends BaseParamParser {
    public EnumParamParser() {
        super(new Class[]{Enum.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        Object enumInst = Reflections.getEnum(paramType, param);

        if (enumInst == null) {
            return Response.failure("枚举不合法");
        }

        return Response.success(enumInst);
    }
}
