package com.github.julyss2019.bukkit.voidframework.internal;

import com.github.julyss2019.bukkit.voidframework.VoidFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandManager;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.common.Plugins;
import com.github.julyss2019.bukkit.voidframework.internal.command.DemoACommandGroup;
import com.github.julyss2019.bukkit.voidframework.internal.command.DemoCommandGroup;
import com.github.julyss2019.bukkit.voidframework.internal.command.PluginCommandGroup;
import com.github.julyss2019.bukkit.voidframework.internal.listener.PluginUnregisterListener;
import com.github.julyss2019.bukkit.voidframework.internal.task.ConsoleAppenderFlushTask;
import com.github.julyss2019.bukkit.voidframework.internal.task.LoggerDailyFileAppenderAutoFlushTask;
import com.github.julyss2019.bukkit.voidframework.locale.LocaleParser;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.locale.resource.YamlLocaleResource;
import com.github.julyss2019.bukkit.voidframework.logging.LogManager;
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger;
import com.github.julyss2019.bukkit.voidframework.thirdparty.VaultThirdParty;
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
public class VoidFrameworkPlugin extends JavaPlugin {
    private static VoidFrameworkPlugin inst;
    private Logger pluginLogger;
    private LogManager logManager;
    private CommandManager commandManager;
    private CommandFramework commandFramework;
    private ConsoleAppenderFlushTask consoleAppenderFlushTask;
    private LocaleResource localeResource;
    private boolean successInit;

    @Override
    public void onEnable() {
        inst = this;

        VoidFramework.setInst(this);
        saveResources();
        setLocaleResource();

        this.logManager = new LogManager(this);
        this.pluginLogger = logManager.createSimpleLogger(this);
        this.commandManager = new CommandManager(this);
        this.commandFramework = commandManager.createCommandFramework(this);

        commandFramework.registerCommandGroup(new PluginCommandGroup(this));
        commandFramework.registerCommandGroup(new DemoCommandGroup());
        commandFramework.registerCommandGroup(new DemoACommandGroup());
        Bukkit.getPluginManager().registerEvents(new PluginUnregisterListener(this), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        runTasks();

        try {
            setupVaultEconomy();
        } catch (Throwable ignored) {

        }

        pluginLogger.info("插件已加载.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        commandManager.unregisterAllCommandFrameworks();
        pluginLogger.info("插件已卸载.");
        logManager.unregisterAllLoggers();
    }

    public void reload() {
        saveResources();
        localeResource.reload();
        pluginLogger.info("插件已重载.");
    }

    private void setupVaultEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (economyProvider != null) {
            Economy provider = economyProvider.getProvider();

            VaultThirdParty.setVaultEconomy(provider);
        }
    }

    public void setLocaleResource() {
        Yaml yaml = Yaml.fromPluginConfigFile(this);
        Locale locale = LocaleParser.parse(yaml.getString("locale"));

        this.localeResource = YamlLocaleResource.fromPluginLocaleFolder(locale, this);
    }

    public LocaleResource getLocaleResource() {
        return localeResource;
    }

    private void saveResources(){
        Plugins.savePluginResource(this, "config", getDataFolder(), false);
    }

    private void runTasks() {
        new LoggerDailyFileAppenderAutoFlushTask(this).runTaskTimerAsynchronously(this, 0L, 20L);
        (this.consoleAppenderFlushTask = new ConsoleAppenderFlushTask(this)).runTaskTimer(this, 0L, 20L);
    }

    public ConsoleAppenderFlushTask getConsoleAppenderFlushTask() {
        return consoleAppenderFlushTask;
    }

    public static VoidFrameworkPlugin getInst() {
        return inst;
    }

    public Logger getPluginLogger() {
        return pluginLogger;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }
}
