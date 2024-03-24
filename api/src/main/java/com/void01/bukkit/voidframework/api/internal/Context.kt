package com.void01.bukkit.voidframework.api.internal

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.api.common.redis.RedisManager
import com.void01.bukkit.voidframework.api.common.script.ScriptManager

interface Context {
    val libraryManager: LibraryManager
    val dataSourceManager: DataSourceManager
    val groovyManager: GroovyManager
    val sharedDataSourceManager: SharedDataSourceManager
    val mongoDbManager: MongoDbManager
    val redisManager: RedisManager
    val scriptManager: ScriptManager
}
