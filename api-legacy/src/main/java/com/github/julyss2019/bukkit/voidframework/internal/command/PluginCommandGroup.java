package com.github.julyss2019.bukkit.voidframework.internal.command;

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@CommandMapping(value = "plugin")
public class PluginCommandGroup  implements CommandGroup {
    private final LegacyVoidFrameworkPlugin plugin;

    public PluginCommandGroup(@NonNull LegacyVoidFrameworkPlugin plugin) {
        this.plugin = plugin;
    }

    @CommandBody(value = "reload", description = "重载插件")
    public void reload(CommandSender sender) {
        plugin.reload();
        sender.sendMessage("重载完毕.");
    }
}
