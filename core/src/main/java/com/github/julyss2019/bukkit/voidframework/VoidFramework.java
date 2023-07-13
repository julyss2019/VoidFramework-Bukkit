package com.github.julyss2019.bukkit.voidframework;

import com.github.julyss2019.bukkit.voidframework.command.CommandManager;
import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.logging.LogManager;
import lombok.NonNull;

public class VoidFramework {
    private static VoidFrameworkPlugin inst;

    public static void setInst(@NonNull VoidFrameworkPlugin inst) {
        if (VoidFramework.inst != null) {
            throw new UnsupportedOperationException();
        }

        VoidFramework.inst = inst;
    }

    public static LogManager getLogManager() {
        return inst.getLogManager();
    }

    public static CommandManager getCommandManager() {
        return inst.getCommandManager();
    }
}
