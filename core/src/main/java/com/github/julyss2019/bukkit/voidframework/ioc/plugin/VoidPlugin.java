package com.github.julyss2019.bukkit.voidframework.ioc.plugin;

import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidPlugin extends JavaPlugin {
    private final Logger internalLogger;

    public VoidPlugin() {
        this(VoidFrameworkPlugin.getInst().getPluginLogger());
    }

    public VoidPlugin(@NonNull Logger logger) {
        this.internalLogger = logger;
    }

    @Override
    public void onEnable() {
        internalLogger.info("load plugin: " + getClass().getName());

        new BeanResolver(this).resolve();
    }

    @Override
    public void onDisable() {
        internalLogger.info("unload plugin: " + getClass().getName());
    }
}
