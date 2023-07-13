package com.github.julyss2019.bukkit.voidframework.command.failure;

import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import org.bukkit.command.CommandSender;

/**
 * 命令失败处理接口
 * 处理以下场景：
 * - 权限不足
 * - 格式错误
 * - 参数解析错误
 * - 执行者不匹配
 */
public interface CommandFailureHandler {
    /**
     * 缺少权限被拒绝
     */
    void onMissingPermission(CommandSender sender,
                             CommandTree commandTree,
                             String[] commandLineArray,
                             String[] availablePermissions);

    /**
     * 命令格式错误
     */
    void onCommandFormatError(CommandSender sender,
                              CommandTree commandTree,
                              String[] commandLineArray);

    /**
     * 命令参数解析错误
     */
    void onCommandParamParseError(CommandSender sender,
                                  CommandTree commandTree,
                                  String[] commandLineArray,
                                  int paramIndex,
                                  String errorMessage);

    /**
     * 命令发送者不匹配
     */
    void onCommandSenderMismatch(CommandSender sender,
                                 CommandTree commandTree,
                                 String[] commandLineArray);
}
