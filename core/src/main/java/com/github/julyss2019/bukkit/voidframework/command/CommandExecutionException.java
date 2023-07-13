package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.command.internal.CommandGroupHolder;
import lombok.NonNull;

public class CommandExecutionException extends RuntimeException {
    private CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CommandExecutionException newException(@NonNull String[] commandLineArray, @NonNull CommandGroupHolder commandGroupHolder, @NonNull String message) {
        return newException(commandLineArray, commandGroupHolder, message, null);
    }

    public static CommandExecutionException newException(@NonNull String[] commandLineArray, @NonNull CommandGroupHolder commandGroupHolder, @NonNull String message, @Nullable Throwable cause) {
        String formatted = String.format("an exception occurred while executing the command: %s(%s): %s",
                String.join(" ", commandLineArray), commandGroupHolder.getCommandGroup().getClass().getName(), message);

        throw new CommandExecutionException(formatted, cause);
    }
}
