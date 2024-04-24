@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.api.common.datasource.shared

@Deprecated("Deprecated")
interface SharedDataSourceManager {
    @Deprecated(message = "命名优化", replaceWith = ReplaceWith("getDataSource2OrNull(id)"))
    fun getDataSource(id: String): SharedDataSource?

    @Deprecated(message = "命名优化", replaceWith = ReplaceWith("getDataSource2(id)"))
    fun getDataSourceOrThrow(id: String): SharedDataSource
}