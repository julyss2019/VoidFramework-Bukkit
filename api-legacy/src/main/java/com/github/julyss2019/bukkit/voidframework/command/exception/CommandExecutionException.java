package com.github.julyss2019.bukkit.voidframework.command.exception;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import lombok.NonNull;

public class CommandExecutionException extends RuntimeException {
    private CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }


    public static CommandExecutionException newException(@NonNull String cli, @Nullable Throwable cause) {
        String formatted = String.format("an exception occurred while executing command: '%s'", String.join(" ", cli));
        throw new CommandExecutionException(formatted, cause);
    }
}
