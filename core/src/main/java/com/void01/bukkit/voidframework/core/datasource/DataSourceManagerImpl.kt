package com.void01.bukkit.voidframework.core.datasource

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.api.common.datasource.CloseableDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class DataSourceManagerImpl : DataSourceManager {
    override fun getHikari(jdbcUrl: String, userName: String, password: String): CloseableDataSource {
        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = userName
            this.password = password
        }
        val dataSource = HikariDataSource(config)

        return object : CloseableDataSourceImpl(dataSource) {
            override fun isClosed(): Boolean {
                return dataSource.isClosed
            }

            override fun close() {
                dataSource.close()
            }
        }
    }
}