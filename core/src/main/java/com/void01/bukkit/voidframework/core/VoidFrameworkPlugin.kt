package com.void01.bukkit.voidframework.core

import com.github.julyss2019.bukkit.voidframework.VoidFramework
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger
import com.void01.bukkit.voidframework.api.common.JavaVoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework3
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.extension.VoidPlugin
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.DependencyLoader
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.api.common.redis.RedisManager
import com.void01.bukkit.voidframework.api.common.script.ScriptManager
import com.void01.bukkit.voidframework.api.internal.Context
import com.void01.bukkit.voidframework.core.datasource.DataSourceManagerImpl
import com.void01.bukkit.voidframework.core.datasource.shared.SharedDataSourceManagerImpl
import com.void01.bukkit.voidframework.core.groovy.GroovyManagerImpl
import com.void01.bukkit.voidframework.core.internal.MainCommandGroup
import com.void01.bukkit.voidframework.core.library.LibraryManagerImpl
import com.void01.bukkit.voidframework.core.mongodb.MongoDbManagerImpl
import com.void01.bukkit.voidframework.core.redis.RedisManagerImpl
import com.void01.bukkit.voidframework.core.script.ScriptManagerImpl
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
class VoidFrameworkPlugin : VoidPlugin(), Context {
    private var legacy: LegacyVoidFrameworkPlugin? = null
    override var libraryManager: LibraryManager
        private set
    override lateinit var groovyManager: GroovyManager
        private set
    override lateinit var sharedDataSourceManager: SharedDataSourceManager
        private set
    override lateinit var mongoDbManager: MongoDbManager
        private set
    override lateinit var dataSourceManager: DataSourceManager
        private set
    override lateinit var redisManager: RedisManager
        private set
    override lateinit var scriptManager: ScriptManager
        private set


    init {
        libraryManager = LibraryManagerImpl(this)

        loadDependencies()
        VoidFramework3.setContext(this)
        VoidFramework2.setContext(this)
        JavaVoidFramework2.setContext(this)
    }

    private fun loadDependencies() {
        val dependencyLoader = DependencyLoader(libraryManager)

        // Kotlin
        dependencyLoader.load(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.20", Relocation.createShadowSafely("_kotlin_", "_kotlin.v1_9_20_")
        )
        dependencyLoader.load(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.20", Relocation.createShadowSafely("_kotlin_", "_vf.kotlin_")
        )
        // HikariCP
        dependencyLoader.load(
            "com.zaxxer:HikariCP:4.0.3", Relocation.createShadowSafely("_com.zaxxer.hikari_", "_vf.com.zaxxer.hikari_")
        )
        // MongoDB
        dependencyLoader.load(
            "org.mongodb:mongodb-driver-sync:4.11.1",
            Relocation.createShadowSafely("_com.mongodb_", "_vf.com.mongodb_"),
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
        dependencyLoader.load(
            "org.mongodb:bson:4.11.1",
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
        dependencyLoader.load(
            "org.mongodb:mongodb-driver-core:4.11.1",
            Relocation.createShadowSafely("_com.mongodb_", "_vf.com.mongodb_"),
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
        // Redis
        dependencyLoader.load("redis.clients:jedis:5.1.2", Relocation.createShadowSafely("_redis_", "_vf.redis_"))
    }

    override fun onPluginEnable() {
        legacy = LegacyVoidFrameworkPlugin(this)
        legacy!!.onEnable()

        dataSourceManager = DataSourceManagerImpl()
        groovyManager = GroovyManagerImpl(this)
        sharedDataSourceManager = SharedDataSourceManagerImpl(this)
        mongoDbManager = MongoDbManagerImpl(this)
        redisManager = RedisManagerImpl(this)

        VoidFramework.getCommandManager().createCommandFramework(this).apply {
            registerCommandGroup(MainCommandGroup(this@VoidFrameworkPlugin))
        }
    }

    override fun onPluginDisable() {
        legacy!!.onDisable()
        (mongoDbManager as MongoDbManagerImpl).closeAll()
    }

    fun reload() {
        legacy!!.reload()
        (scriptManager as ScriptManagerImpl).reload()
    }

    fun getScriptsDir(): File {
        return File(dataFolder, "scripts")
    }
}
