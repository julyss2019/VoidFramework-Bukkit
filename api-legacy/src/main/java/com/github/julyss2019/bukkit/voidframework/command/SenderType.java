package com.github.julyss2019.bukkit.voidframework.command;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * 发送者类型
 */
public enum SenderType {
    PLAYER, CONSOLE;

    /**
     * 获取 CommandSender 所对应的发送者类型
     */
    public static SenderType of(@NonNull CommandSender sender) {
        if (sender instanceof Player) {
            return PLAYER;
        } else if (sender instanceof ConsoleCommandSender) {
            return CONSOLE;
        }

        throw new UnsupportedOperationException("unknown command sender: " + sender);
    }
}
