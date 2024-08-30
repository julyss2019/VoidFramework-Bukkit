package com.void01.bukkit.voidframework.api.common.database

import java.sql.Connection
import java.sql.ResultSet

class DbResult(private val connection: Connection, private val resultSet: ResultSet) : ResultSet by resultSet {
    override fun close() {
        resultSet.close()
        connection.close()
    }
}