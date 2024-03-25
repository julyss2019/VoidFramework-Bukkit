package com.void01.bukkit.voidframework.api.common.mongodb

interface MongoDbManager {
    @Deprecated(message = "弃用")
    fun getSharedMongoDbClientOrNull(id: String): Any?

    @Deprecated(message = "弃用")
    fun getSharedMongoDbClient(id: String): Any

    fun getSharedClientOrNull(id: String): Any?

    fun getSharedClient(id: String): Any
}