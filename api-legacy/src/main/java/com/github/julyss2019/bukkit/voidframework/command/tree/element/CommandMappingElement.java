package com.github.julyss2019.bukkit.voidframework.command.tree.element;

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.command.CommandGroupContext;
import lombok.NonNull;


/**
 * 命令映射元素
 */
public class CommandMappingElement extends BaseCommandElement {
    /**
     * 构造函数
     * @param commandGroupContext
     * @param id 因为要使用 /a/b 多重映射, 所以需要手动指定
     * @param annotation
     */
    public CommandMappingElement(@NonNull CommandGroupContext commandGroupContext, String id, CommandMapping annotation) {
        super(commandGroupContext, id, annotation.permission());
    }
}
