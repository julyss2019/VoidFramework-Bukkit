package com.void01.bukkit.voidframework.api.common.datasource

import javax.sql.DataSource

/*
为什么不直接使用 HikariDataSource ？
因为 relocate 的问题，导致 API 发布后包名都对不上了
为什么不使用 DataSource？
DataSource 没有 close()
 */
/**
 * 简单的连接池
 * 额外提供了 [isClosed] [close] 方法
 */
@Deprecated("使用 SharedDataSource")
interface CloseableDataSource : DataSource {
    fun isClosed() : Boolean

    fun close()
}