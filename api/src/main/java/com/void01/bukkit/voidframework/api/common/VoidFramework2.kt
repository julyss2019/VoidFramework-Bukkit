package com.void01.bukkit.voidframework.api.common

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.internal.Context

@Deprecated("java 调用 kotlin object 需要有奇怪的 INSTANCE 前缀", replaceWith = ReplaceWith("VoidFramework3"))
object VoidFramework2 {
    private lateinit var context: Context

    fun setContext(context: Context) {
        if (VoidFramework2::context.isInitialized) {
            throw UnsupportedOperationException()
        }

        VoidFramework2.context = context
    }

    fun getGroovyManager() : GroovyManager {
        return context.groovyManager
    }

    fun getLibraryManager(): LibraryManager {
        return context.libraryManager
    }

    @Deprecated(message = "弃用")
    fun getDataSourceManager() : DataSourceManager {
        return context.dataSourceManager
    }

    fun getSharedDataSourceManager() : SharedDataSourceManager {
        return context.sharedDataSourceManager
    }
}