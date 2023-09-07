package com.github.julyss2019.bukkit.voidframework;

import com.github.julyss2019.bukkit.voidframework.command.CommandManager;
import com.github.julyss2019.bukkit.voidframework.logging.LogManager;
import lombok.Getter;

public class VoidFramework {
    @Getter
    private static LogManager logManager;
    @Getter
    private static CommandManager commandManager;


    public static void setLogManager(LogManager logManager) {
        if (VoidFramework.logManager != null) {
            throw new UnsupportedOperationException();
        }

        VoidFramework.logManager = logManager;
    }

    public static void setCommandManager(CommandManager commandManager) {
        if (VoidFramework.commandManager != null) {
            throw new UnsupportedOperationException();
        }

        VoidFramework.commandManager = commandManager;
    }
}
