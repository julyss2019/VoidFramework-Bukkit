package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class OfflinePlayerParamParser extends BaseParamParser {
    public OfflinePlayerParamParser() {
        super(new Class[]{OfflinePlayer.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        return Response.success(Bukkit.getOfflinePlayer(param));
    }
}
