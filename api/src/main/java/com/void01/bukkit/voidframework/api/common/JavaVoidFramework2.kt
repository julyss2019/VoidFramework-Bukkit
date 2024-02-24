package com.void01.bukkit.voidframework.api.common

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.internal.Context

@Deprecated("弃用")
object JavaVoidFramework2 {
    private lateinit var context: Context

    @JvmStatic
    fun setContext(context: Context) {
        if (JavaVoidFramework2::context.isInitialized) {
            throw UnsupportedOperationException()
        }

        JavaVoidFramework2.context = context
    }

    @JvmStatic
    fun getGroovyManager() : GroovyManager {
        return context.groovyManager
    }

    @JvmStatic
    fun getLibraryManager(): LibraryManager {
        return context.libraryManager
    }

    @Deprecated(message = "弃用")
    @JvmStatic
    fun getDataSourceManager() : DataSourceManager {
        return context.dataSourceManager
    }

    @JvmStatic
    fun getSharedDataSourceManager() : SharedDataSourceManager {
        return context.sharedDataSourceManager
    }
}