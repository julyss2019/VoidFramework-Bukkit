package com.github.julyss2019.bukkit.voidframework.command.internal.util;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Map;

public class BukkitCommandUtil {
    private static final  Field commandMapField;
    private static final Field knownCommandsField;

    static {
        try {
            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            knownCommandsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    private static Map<String, Command> getKnownCommands() {
        try {
            return (Map<String, Command>) knownCommandsField.get(getCommandMap());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static CommandMap getCommandMap() {
        try {
            return (CommandMap) commandMapField.get(Bukkit.getPluginManager());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean existsCommand(@NonNull String id) {
        return getCommandMap().getCommand(id) != null;
    }

    public static void unregisterCommand(@NonNull String id) {
        getKnownCommands().remove(id);
    }

    public static void registerCommand(@NonNull String id, @NonNull Command command) {
        if (existsCommand(id)) {
            throw new IllegalArgumentException(String.format("command '%s' already registered", id));
        }

        getCommandMap().register(id, command);
    }
}
