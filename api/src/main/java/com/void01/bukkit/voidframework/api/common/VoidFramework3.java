package com.void01.bukkit.voidframework.api.common;

import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager;
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager;
import com.void01.bukkit.voidframework.api.common.library.LibraryManager;
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager;
import com.void01.bukkit.voidframework.api.common.redission.RedissonManager;
import com.void01.bukkit.voidframework.api.common.script.ScriptManager;
import com.void01.bukkit.voidframework.api.internal.Context;

public class VoidFramework3 {
    private static Context context;

    public static void setContext(Context context) {
        if (VoidFramework3.context != null) {
            throw new UnsupportedOperationException();
        }

        VoidFramework3.context = context;
    }

    public static LibraryManager getLibraryManager() {
        return context.getLibraryManager();
    }

    public static GroovyManager getGroovyManager() {
        return context.getGroovyManager();
    }

    public static SharedDataSourceManager getSharedDataSourceManager() {
        return context.getSharedDataSourceManager();
    }

    public static MongoDbManager getMongoDbManager() {
        return context.getMongoDbManager();
    }

    public static RedissonManager getRedisManager() {
        return context.getRedissonManager();
    }

    public static ScriptManager getScriptManager() {
        return context.getScriptManager();
    }
}