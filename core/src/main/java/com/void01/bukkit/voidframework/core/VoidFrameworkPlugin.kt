package com.void01.bukkit.voidframework.core

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger
import com.void01.bukkit.voidframework.api.common.VoidFramework2
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.Library
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.common.library.Repository
import com.void01.bukkit.voidframework.api.internal.Context
import com.void01.bukkit.voidframework.core.datasource.DataSourceManagerImpl
import com.void01.bukkit.voidframework.core.datasource.shared.SharedDataSourceManagerImpl
import com.void01.bukkit.voidframework.core.groovy.GroovyManagerImpl
import com.void01.bukkit.voidframework.core.library.LibraryManagerImpl
import org.bukkit.plugin.java.JavaPlugin

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
class VoidFrameworkPlugin : JavaPlugin(), Context {
    private var legacy: LegacyVoidFrameworkPlugin? = null

    override var libraryManager: LibraryManager
        private set
    override lateinit var dataSourceManager: DataSourceManager
        private set
    override lateinit var groovyManager: GroovyManager
        private set
    override lateinit var sharedDataSourceManager: SharedDataSourceManager
        private set
    lateinit var voidLogger: Logger
        private set

    // 提前加载
    init {
        libraryManager = LibraryManagerImpl(this)
        libraryManager.load(
            Library.Builder
                .create()
                .setClassLoaderByBukkitPlugin(this)
                .setDependencyByGradleStyleExpression("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
                .addRepositories(Repository.ALIYUN, Repository.CENTRAL)
                .addSafeRelocation("_kotlin_", "_com.void01.bukkit.voidframework.core.lib.kotlin_")
                .setResolveRecursively(true)
                .build()
        )
        libraryManager.load(
            Library.Builder
                .create()
                .setClassLoaderByBukkitPlugin(this)
                .setDependencyByGradleStyleExpression("com.zaxxer:HikariCP:4.0.3")
                .addRepositories(Repository.ALIYUN, Repository.CENTRAL)
                .addSafeRelocation(
                    "_com.zaxxer.hikari_",
                    "_com.void01.bukkit.voidframework.core.lib.com.zaxxer.hikari_"
                )
                .build()
        )
        VoidFramework2.setContext(this)
    }

    override fun onEnable() {
        legacy = LegacyVoidFrameworkPlugin(this)
        legacy!!.onEnable()
        voidLogger = legacy!!.pluginLogger

        dataSourceManager = DataSourceManagerImpl()
        groovyManager = GroovyManagerImpl(this)
        sharedDataSourceManager = SharedDataSourceManagerImpl(this)

        // 预加载，第一次加载需要时间
        groovyManager.eval("1+1")
    }

    override fun onDisable() {
        legacy!!.onDisable()
    }
}
