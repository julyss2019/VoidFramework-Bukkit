package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerParamParser extends BaseParamParser {
    public PlayerParamParser() {
        super(new Class[]{Player.class});
    }

    @Override
    public Response parse(CommandSender sender, Class<?> paramType, String param) {
        Player player = Bukkit.getPlayer(param);

        // 玩家 j 离线时，july_ss 在线时，Bukkit.getPlayer("j") 会返回 july_ss
        if (player == null || !player.getName().equals(param)) {
            return Response.failure("玩家离线");
        }

        return Response.success(player);
    }
}
