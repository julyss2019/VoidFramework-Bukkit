package com.void01.bukkit.voidframework.api.common.datasource

import javax.sql.DataSource

interface DataSourceManager {
    fun getDataSource(id: String): DataSource

    fun getDataSourceOrNull(id: String): DataSource?
}