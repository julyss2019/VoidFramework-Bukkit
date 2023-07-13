package com.github.julyss2019.bukkit.voidframework.command.helper;

import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import org.bukkit.command.CommandSender;

/**
 * 命令帮助者接口
 */
public interface CommandHelper {
    /**
     * 处理帮助
     * @param sender 命令发送者
     * @param commandTree 命令树
     */
    void onHelp(CommandSender sender, CommandTree commandTree, String[] commandLineArray);
}
