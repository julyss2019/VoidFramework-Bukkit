package com.github.julyss2019.bukkit.voidframework.internal;

import com.github.julyss2019.bukkit.voidframework.VoidFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandManager;
import com.github.julyss2019.bukkit.voidframework.common.Plugins;
import com.github.julyss2019.bukkit.voidframework.internal.listener.PluginUnregisterListener;
import com.github.julyss2019.bukkit.voidframework.internal.logger.LegacyPluginLogger;
import com.github.julyss2019.bukkit.voidframework.internal.logger.Level;
import com.github.julyss2019.bukkit.voidframework.internal.task.ConsoleAppenderFlushTask;
import com.github.julyss2019.bukkit.voidframework.internal.task.LoggerDailyFileAppenderAutoFlushTask;
import com.github.julyss2019.bukkit.voidframework.locale.LocaleParser;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.locale.resource.YamlLocaleResource;
import com.github.julyss2019.bukkit.voidframework.logging.LogManager;
import com.github.julyss2019.bukkit.voidframework.thirdparty.VaultThirdParty;
import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue;
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.Locale;

public class LegacyVoidFrameworkPlugin {
    private static Plugin plugin;
    private LegacyPluginLogger legacyPluginLogger;
    private LogManager logManager;
    private CommandManager commandManager;
    private CommandFramework commandFramework;
    private ConsoleAppenderFlushTask consoleAppenderFlushTask;
    private LocaleResource localeResource;

    public LegacyVoidFrameworkPlugin(@NonNull Plugin plugin) {
        LegacyVoidFrameworkPlugin.plugin = plugin;
    }

    public static Plugin getInstance() {
        return plugin;
    }

    public void onEnable() {
        saveResources();
        setLocaleResource();

        this.logManager = new LogManager(this);
        this.legacyPluginLogger = new LegacyPluginLogger(plugin);

        File legacyLogFile = new File(plugin.getDataFolder(), "config.yml");

        if (legacyLogFile.exists()) {
            Yaml legacyLogYaml = Yaml.fromFile(legacyLogFile);

            legacyPluginLogger.setThreshold(legacyLogYaml.getEnum("log.threshold", Level.class, DefaultValue.of(Level.INFO)));
        }

        this.commandManager = new CommandManager(this);
        this.commandFramework = commandManager.createCommandFramework(plugin);

        VoidFramework.setCommandManager(commandManager);
        VoidFramework.setLogManager(logManager);

        Bukkit.getPluginManager().registerEvents(new PluginUnregisterListener(this), plugin);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        runTasks();

        try {
            setupVaultEconomy();
        } catch (Throwable ignored) {
        }
    }

    public void onDisable() {
        HandlerList.unregisterAll(plugin);
        commandManager.unregisterAllCommandFrameworks();
        legacyPluginLogger.info("插件已卸载.");
        logManager.unregisterAllLoggers();
    }

    public void reload() {
        saveResources();
        localeResource.reload();
    }

    private void setupVaultEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (economyProvider != null) {
            Economy provider = economyProvider.getProvider();

            VaultThirdParty.setVaultEconomy(provider);
        }
    }

    public void setLocaleResource() {
        Yaml yaml = Yaml.fromPluginConfigFile(plugin);
        Locale locale = LocaleParser.parse(yaml.getString("locale"));

        this.localeResource = YamlLocaleResource.fromPluginLocaleFolder(locale, plugin);
    }

    public LocaleResource getLocaleResource() {
        return localeResource;
    }

    private void saveResources() {
        Plugins.savePluginResource(plugin, "config", plugin.getDataFolder(), false);
    }

    private void runTasks() {
        new LoggerDailyFileAppenderAutoFlushTask(this).runTaskTimerAsynchronously(plugin, 0L, 20L);
        (this.consoleAppenderFlushTask = new ConsoleAppenderFlushTask(this)).runTaskTimer(plugin, 0L, 1L);
    }

    public ConsoleAppenderFlushTask getConsoleAppenderFlushTask() {
        return consoleAppenderFlushTask;
    }

    public LegacyPluginLogger getPluginLogger() {
        return legacyPluginLogger;
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

    public File getLibsDir() {
        return new File(plugin.getDataFolder(), "libs");
    }
}
