@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.api.internal

import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.api.common.redission.RedissonManager
import com.void01.bukkit.voidframework.api.common.script.ScriptManager

interface Context {
    val libraryManager: LibraryManager
    val groovyManager: GroovyManager
    val dataSourceManager: DataSourceManager
    val sharedDataSourceManager: SharedDataSourceManager
    val mongoDbManager: MongoDbManager
    val redissonManager: RedissonManager
    val scriptManager: ScriptManager
    val componentManager: ComponentManager
}
