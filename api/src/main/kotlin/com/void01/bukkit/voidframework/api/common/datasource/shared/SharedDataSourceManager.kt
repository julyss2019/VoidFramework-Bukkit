package com.void01.bukkit.voidframework.api.common.datasource.shared

import java.lang.IllegalArgumentException

interface SharedDataSourceManager {
    fun getDataSource(id:String) : SharedDataSource?

    fun getDataSourceOrThrow(id: String) : SharedDataSource {
        return getDataSource(id) ?: throw IllegalArgumentException("Unable to find datasource: $id")
    }
}