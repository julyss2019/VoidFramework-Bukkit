package com.github.julyss2019.bukkit.voidframework.command.tree.element;

import com.github.julyss2019.bukkit.voidframework.command.CommandGroupContext;

/**
 * 命令元素
 * 从根命令树下会衍生出 N 个命令元素, 命令元素包括：{@link CommandBodyElement}, {@link CommandMappingElement}
 */
public interface CommandElement {
    /**
     * 命令 Id
     */
    String getCommandId();

    /**
     * 命令权限
     */
    String getPermission();

    /**
     * 持有者
     */
    CommandGroupContext getActiveCommandGroup();
}
