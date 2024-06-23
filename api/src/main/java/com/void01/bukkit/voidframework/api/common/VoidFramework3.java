package com.void01.bukkit.voidframework.api.common;

import com.github.julyss2019.bukkit.voidframework.VoidFramework;
import com.github.julyss2019.bukkit.voidframework.command.CommandManager;
import com.void01.bukkit.voidframework.api.common.component.ComponentManager;
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager;
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager;
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager;
import com.void01.bukkit.voidframework.api.common.library.LibraryManager;
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager;
import com.void01.bukkit.voidframework.api.common.redission.RedissonManager;
import com.void01.bukkit.voidframework.api.common.script.ScriptManager;
import com.void01.bukkit.voidframework.api.internal.Context;

public class VoidFramework3 {
    private static Context context;
    private static ComponentManager componentManager;

    public static void setContext(Context context) {
        if (VoidFramework3.context != null) {
            throw new UnsupportedOperationException();
        }

        VoidFramework3.context = context;
    }

    public static ComponentManager getComponentManager() {
        return componentManager;
    }

    public static void setComponentManager(ComponentManager componentManager) {
        VoidFramework3.componentManager = componentManager;
    }

    public static LibraryManager getLibraryManager() {
        return context.getLibraryManager();
    }

    public static GroovyManager getGroovyManager() {
        return context.getGroovyManager();
    }

    public static MongoDbManager getMongoDbManager() {
        return context.getMongoDbManager();
    }

    public static RedissonManager getRedissonManager() {
        return context.getRedissonManager();
    }

    public static CommandManager getCommandManager() {
        return VoidFramework.getCommandManager();
    }

    public static DataSourceManager getDataSourceManager() {
        return context.getDataSourceManager();
    }

    @Deprecated
    public static SharedDataSourceManager getSharedDataSourceManager() {
        return context.getSharedDataSourceManager();
    }

    @Deprecated
    public static ScriptManager getScriptManager() {
        return context.getScriptManager();
    }
}