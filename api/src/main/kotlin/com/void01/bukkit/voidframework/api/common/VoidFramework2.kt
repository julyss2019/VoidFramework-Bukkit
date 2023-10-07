package com.void01.bukkit.voidframework.api.common

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager
import com.void01.bukkit.voidframework.api.internal.Context

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

    fun getDataSourceManager() : DataSourceManager {
        String
        return context.dataSourceManager
    }
}