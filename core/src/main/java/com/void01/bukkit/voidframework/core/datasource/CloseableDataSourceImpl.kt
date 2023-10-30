package com.void01.bukkit.voidframework.core.datasource

import com.void01.bukkit.voidframework.api.common.datasource.CloseableDataSource
import java.io.PrintWriter
import java.sql.Connection
import java.util.logging.Logger
import javax.sql.DataSource

@Deprecated(message = "弃用")
abstract class CloseableDataSourceImpl(private val dataSource: DataSource) : CloseableDataSource {
    override fun getLogWriter(): PrintWriter {
        return dataSource.logWriter
    }

    override fun setLogWriter(out: PrintWriter?) {
        dataSource.logWriter = out
    }

    override fun setLoginTimeout(seconds: Int) {
        dataSource.loginTimeout
    }

    override fun getLoginTimeout(): Int {
        return dataSource.loginTimeout
    }

    override fun getParentLogger(): Logger {
        return dataSource.parentLogger
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        return dataSource.unwrap(iface)
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return dataSource.isWrapperFor(iface)
    }

    override fun getConnection(): Connection {
        return dataSource.connection
    }

    override fun getConnection(username: String?, password: String?): Connection {
        return dataSource.getConnection(username, password)
    }
}