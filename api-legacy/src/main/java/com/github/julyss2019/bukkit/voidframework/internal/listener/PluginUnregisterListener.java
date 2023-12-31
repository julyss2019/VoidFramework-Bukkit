package com.github.julyss2019.bukkit.voidframework.internal.listener;

import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@RequiredArgsConstructor
public class PluginUnregisterListener implements Listener {
    private final LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin;

    @EventHandler
    public void onPluginDisableEvent(PluginDisableEvent event) {
//        voidFrameworkPlugin.getCommandManager().unregisterCommandFrameworks(event.getPlugin());
//        voidFrameworkPlugin.getLogManager().unregisterLoggers(event.getPlugin());
    }
}
