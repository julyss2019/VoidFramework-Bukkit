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
    private static final CommandMap COMMAND_MAP;
    private static final Map<String, Command> KNOWN_COMMANDS;

    static {
        try {
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            COMMAND_MAP = (CommandMap) commandMapField.get(Bukkit.getPluginManager());
            commandMapField.setAccessible(false);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");

            knownCommandsField.setAccessible(true);
            //noinspection unchecked
            KNOWN_COMMANDS = (Map<String, Command>) knownCommandsField.get(COMMAND_MAP);
            knownCommandsField.setAccessible(false);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean existsCommand(@NonNull String id) {
        return COMMAND_MAP.getCommand(id) != null;
    }

    public static void unregisterCommand(@NonNull String id) {
        KNOWN_COMMANDS.remove(id);
    }

    public static void registerCommand(@NonNull String id, @NonNull Command command) {
        if (existsCommand(id)) {
            throw new IllegalArgumentException(String.format("command '%s' already registered", id));
        }

        COMMAND_MAP.register(id, command);
    }
}
