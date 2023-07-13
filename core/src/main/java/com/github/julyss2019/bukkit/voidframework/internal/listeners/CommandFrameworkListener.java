package com.github.julyss2019.bukkit.voidframework.internal.listeners;

import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@RequiredArgsConstructor
public class CommandFrameworkListener implements Listener {
    private final VoidFrameworkPlugin voidFrameworkPlugin;

    @EventHandler
    public void onPluginDisableEvent(PluginDisableEvent event) {
        voidFrameworkPlugin.getCommandManager().unregisterCommandFrameworks(event.getPlugin());
    }
}
