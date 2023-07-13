package com.github.julyss2019.bukkit.voidframework.command;

import lombok.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CommandGroupVerifyException extends RuntimeException {
    public CommandGroupVerifyException(String message) {
        super(message);
    }

    public static CommandGroupVerifyException newMethodException(@NonNull CommandGroup commandGroup, @NonNull Method method, @NonNull String message) {
        return new CommandGroupVerifyException(String.format("illegal method %s(class: %s): %s", method.getName(), commandGroup.getClass().getName(), message));
    }

    public static CommandGroupVerifyException newIllegalMethodParamException(@NonNull CommandGroup commandGroup, @NonNull Method method, @NonNull Parameter parameter, @NonNull String message) {
        return new CommandGroupVerifyException(String.format("illegal method param %s(method: %s, class: %s): %s", parameter.getName(), method, commandGroup.getClass().getName(), message));
    }
}
