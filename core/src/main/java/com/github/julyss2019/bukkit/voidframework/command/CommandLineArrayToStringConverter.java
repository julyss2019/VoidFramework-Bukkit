package com.github.julyss2019.bukkit.voidframework.command;

import lombok.NonNull;

public class CommandLineArrayToStringConverter {
    public static String convertToString(@NonNull String[] commandLineArray) {
        return "/" + String.join(" ", commandLineArray);
    }
}
