package com.github.julyss2019.bukkit.voidframework.command.internal;

import com.github.julyss2019.bukkit.voidframework.command.CommandFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandGroup;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/**
 * 命令持有者
 */
public class CommandGroupHolder {
    private final Plugin plugin;
    private final CommandFramework commandFramework;
    private final CommandGroup commandGroup;

    public CommandGroupHolder(@NonNull Plugin plugin, @NonNull CommandFramework commandFramework, @NonNull CommandGroup commandGroup) {
        this.plugin = plugin;
        this.commandFramework = commandFramework;
        this.commandGroup = commandGroup;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    public CommandGroup getCommandGroup() {
        return commandGroup;
    }
}
