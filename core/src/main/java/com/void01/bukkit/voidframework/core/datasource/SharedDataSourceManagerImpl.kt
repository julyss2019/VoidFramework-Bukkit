@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.core.datasource

import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSource
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import com.void01.bukkit.voidframework.core.datasource.shared.HikariSharedDataSource
import com.zaxxer.hikari.HikariDataSource

class SharedDataSourceManagerImpl(plugin: VoidFrameworkPlugin) : SharedDataSourceManager {
    private val dataSourceManager = plugin.dataSourceManager

    @Deprecated("命名优化", replaceWith = ReplaceWith("getDataSource2OrNull(id)"))
    override fun getDataSource(id: String): SharedDataSource? {
        return HikariSharedDataSource((dataSourceManager.getDataSourceOrNull(id) ?: return null) as HikariDataSource)
    }

    @Deprecated("命名优化", replaceWith = ReplaceWith("getDataSource2(id)"))
    override fun getDataSourceOrThrow(id: String): SharedDataSource = getDataSource(id) ?: throw IllegalArgumentException("Unable to find DataSource by id: $id")
}