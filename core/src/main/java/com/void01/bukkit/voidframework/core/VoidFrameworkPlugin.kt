@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.core

import com.github.julyss2019.bukkit.voidframework.VoidFramework
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin
import com.void01.bukkit.voidframework.api.common.JavaVoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework3
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.extension.VoidPlugin
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryLoader
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.api.common.redission.RedissonManager
import com.void01.bukkit.voidframework.api.common.script.ScriptManager
import com.void01.bukkit.voidframework.api.internal.Context
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.common.UrlClassLoaderModifier
import com.void01.bukkit.voidframework.common.kotlin.safeShadow
import com.void01.bukkit.voidframework.core.component.ComponentManagerImpl
import com.void01.bukkit.voidframework.core.datasource.DataSourceManagerImpl
import com.void01.bukkit.voidframework.core.datasource.SharedDataSourceManagerImpl
import com.void01.bukkit.voidframework.core.groovy.GroovyManagerImpl
import com.void01.bukkit.voidframework.core.internal.MainCommandGroup
import com.void01.bukkit.voidframework.core.library.LibraryManagerImpl
import com.void01.bukkit.voidframework.core.mongodb.MongoDbManagerImpl
import com.void01.bukkit.voidframework.core.redission.RedissonManagerImpl
import com.void01.bukkit.voidframework.core.script.ScriptManagerImpl
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
class VoidFrameworkPlugin : VoidPlugin(), Context {
    val scriptsPath: Path
        get() {
            return dataFolder.toPath().resolve("scripts")
        }
    val scriptLibsPath: Path
        get() {
            return dataFolder.toPath().resolve("script-libs")
        }
    val componentsPath: Path
        get() {
            return dataFolder.toPath().resolve("components")
        }
    val componentLibsPath: Path
        get() {
            return dataFolder.toPath().resolve("component-libs")
        }

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
    override lateinit var redissonManager: RedissonManager
        private set
    override lateinit var scriptManager: ScriptManager
        private set
    override lateinit var componentManager: ComponentManager
        private set

    init {
        libraryManager = LibraryManagerImpl(this)

        loadLibraries()
        VoidFramework2.setContext(this)
        VoidFramework3.setContext(this);
        JavaVoidFramework2.setContext(this)
    }

    private fun loadLibraries() {
        val libraryLoader = LibraryLoader(libraryManager)

        // Kotlin
        libraryLoader.load(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.20", Relocation.createShadowSafely("_kotlin_", "_kotlin.v1_9_20_")
        )
        libraryLoader.load(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.20", Relocation.createShadowSafely("_kotlin_", "_vf.kotlin_")
        )
        // HikariCP
        libraryLoader.load(
            "com.zaxxer:HikariCP:4.0.3", Relocation.createShadowSafely("_com.zaxxer.hikari_", "_vf.com.zaxxer.hikari_")
        )
        // MongoDB
        libraryLoader.load(
            safeShadow("_org.mongodb:mongodb-driver-sync:4.11.1_"),
            Relocation.createShadowSafely("_com.mongodb_", "_vf.com.mongodb_"),
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
        libraryLoader.load(
            safeShadow("_org.mongodb:bson:4.11.1_"),
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
        libraryLoader.load(
            safeShadow("_org.mongodb:mongodb-driver-core:4.11.1_"),
            Relocation.createShadowSafely("_com.mongodb_", "_vf.com.mongodb_"),
            Relocation.createShadowSafely("_org.bson_", "_vf.org.bson_"),
        )
    }

    override fun onPluginEnable() {
        // 本地库
        FileUtils.listFiles(dataFolder.toPath().resolve("local-libs"), "jar").forEach {
            UrlClassLoaderModifier.addUrl(classLoader, it.toFile())
            pluginLogger.info("已载入本地库: ${it.absolutePathString()}")
        }

        legacy = LegacyVoidFrameworkPlugin(this)
        legacy!!.onEnable()

        groovyManager = GroovyManagerImpl(this)
        dataSourceManager = DataSourceManagerImpl(this)
        sharedDataSourceManager = SharedDataSourceManagerImpl(this)
        mongoDbManager = MongoDbManagerImpl(this)
        redissonManager = RedissonManagerImpl(this)
        scriptManager = ScriptManagerImpl(this)
        componentManager = ComponentManagerImpl(this)

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
        (componentManager as ComponentManagerImpl).reload()
    }
}
