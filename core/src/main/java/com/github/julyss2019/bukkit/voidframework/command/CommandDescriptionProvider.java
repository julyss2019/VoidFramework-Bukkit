package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;

public interface CommandDescriptionProvider {
    void getBodyDescription(CommandTree commandTree);

    void getParamDescription(CommandTree commandTree);
}
