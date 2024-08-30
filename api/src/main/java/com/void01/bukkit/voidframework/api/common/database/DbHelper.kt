package com.void01.bukkit.voidframework.api.common.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class DbHelper(private val dataSource: DataSource) {
    private fun PreparedStatement.setParameters(params: Params) {
        params.params.forEachIndexed { index, any ->
            setObject(index + 1, any)
        }
    }

    private fun newConnection(): Connection {
        return dataSource.connection
    }

    fun <T> selectFirst(sql: String, params: Params, mapper: (ResultSet) -> T): T {
        return selectFirstOrNull(sql, params, mapper) ?: throw IllegalArgumentException("Query result is empty")
    }

    fun <T> selectFirstOrNull(sql: String, params: Params = Params(), mapper: (ResultSet) -> T): T? {
        return selectAll(sql, params, mapper).first()
    }

    fun <T> selectAll(sql: String, params: Params, mapper: (ResultSet) -> T): List<T> {
        newConnection().use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setParameters(params)
                preparedStatement.executeQuery().use { resultSet ->
                    val results = mutableListOf<T>()

                    while (resultSet.next()) {
                        results.add(mapper.invoke(resultSet))
                    }

                    return results
                }
            }
        }
    }

    fun executeUpdate(sql: String, vararg params: Any): Int {
        return executeUpdate(sql, params.toList())
    }

    fun executeUpdate(
        sql: String,
        params: Params,
        returnGeneratedKeys: Boolean = false,
        block: (PreparedStatement) -> Unit = {}
    ): Int {
        return newConnection().use {
            val prepareStatement =
                it.prepareStatement(
                    sql,
                    if (returnGeneratedKeys) PreparedStatement.RETURN_GENERATED_KEYS else PreparedStatement.NO_GENERATED_KEYS
                )

            prepareStatement.use {
                prepareStatement.setParameters(params)
                prepareStatement.executeUpdate().apply {
                    block(prepareStatement)
                }
            }
        }
    }
}