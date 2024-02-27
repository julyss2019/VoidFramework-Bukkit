package com.void01.bukkit.voidframework.core

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger
import com.void01.bukkit.voidframework.api.common.JavaVoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework2
import com.void01.bukkit.voidframework.api.common.VoidFramework3
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.DependencyLoader
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.api.internal.Context
import com.void01.bukkit.voidframework.core.datasource.DataSourceManagerImpl
import com.void01.bukkit.voidframework.core.datasource.shared.SharedDataSourceManagerImpl
import com.void01.bukkit.voidframework.core.groovy.GroovyManagerImpl
import com.void01.bukkit.voidframework.core.library.LibraryManagerImpl
import com.void01.bukkit.voidframework.core.mongodb.MongoDbManagerImpl
import org.bukkit.plugin.java.JavaPlugin

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
class VoidFrameworkPlugin : JavaPlugin(), Context {
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
    lateinit var voidLogger: Logger
        private set

    // 提前加载
    init {
        libraryManager = LibraryManagerImpl(this)

        loadDependencies()
        VoidFramework3.setContext(this)
        VoidFramework2.setContext(this)
        JavaVoidFramework2.setContext(this)
    }

    private fun loadDependencies() {
        val dependencyLoader = DependencyLoader(libraryManager)

        dependencyLoader.load(
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.20",
            Relocation.createShadowSafely("_kotlin_", "_kotlin.v1_9_20_")
        )
        dependencyLoader.load(
            "com.zaxxer:HikariCP:4.0.3",
            Relocation.createShadowSafely("_com.zaxxer.hikari_", "_com.zaxxer.hikari.v4_0_3_")
        )
        dependencyLoader.load(
            "org.mongodb:mongodb-driver-sync:4.11.1",
            Relocation.createShadowSafely("_com.mongodb_", "_com.mongodb.v4_11_1_"),
            Relocation.createShadowSafely("_org.bson_", "_org.bson.v4_11_1_"),
        )
        dependencyLoader.load(
            "org.mongodb:bson:4.11.1",
            Relocation.createShadowSafely("_org.bson_", "_org.bson.v4_11_1_"),
        )
        dependencyLoader.load(
            "org.mongodb:mongodb-driver-core:4.11.1",
            Relocation.createShadowSafely("_com.mongodb_", "_com.mongodb.v4_11_1_"),
            Relocation.createShadowSafely("_org.bson_", "_org.bson.v4_11_1_"),
        )

    }

    override fun onEnable() {
        legacy = LegacyVoidFrameworkPlugin(this)
        legacy!!.onEnable()
        voidLogger = legacy!!.pluginLogger

        dataSourceManager = DataSourceManagerImpl()
        groovyManager = GroovyManagerImpl(this)
        sharedDataSourceManager = SharedDataSourceManagerImpl(this)
        mongoDbManager = MongoDbManagerImpl(this)
    }

    override fun onDisable() {
        legacy!!.onDisable()
        (mongoDbManager as MongoDbManagerImpl).closeAll()
    }
}
