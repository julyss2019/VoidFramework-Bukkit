package com.github.julyss2019.bukkit.voidframework.common;

import static org.bukkit.Bukkit.getServer;

public class Servers {
    public static String getVersion() {
        String packageName = getServer().getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
