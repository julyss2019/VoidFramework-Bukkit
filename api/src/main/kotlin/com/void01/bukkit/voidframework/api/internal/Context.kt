package com.void01.bukkit.voidframework.api.internal

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.LibraryManager

interface Context {
    val libraryManager: LibraryManager
    val dataSourceManager: DataSourceManager
    val groovyManager: GroovyManager
}
