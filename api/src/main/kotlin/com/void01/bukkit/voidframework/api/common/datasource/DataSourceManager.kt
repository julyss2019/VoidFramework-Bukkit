package com.void01.bukkit.voidframework.api.common.datasource

/**
 * 连接池管理器
 */
@Deprecated("使用 SharedDataSourceManager")
interface DataSourceManager {
    /**
     * 获取一个 Hikari 连接池
     * 除了参数外，其他配置均为默认值
     */
    fun getHikari(jdbcUrl: String, userName: String, password: String): CloseableDataSource
}