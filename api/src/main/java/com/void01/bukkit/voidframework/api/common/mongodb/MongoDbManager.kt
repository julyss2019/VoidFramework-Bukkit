package com.void01.bukkit.voidframework.api.common.mongodb

interface MongoDbManager {
    fun getSharedMongoDbClientOrNull(id: String): Any?

    fun getSharedMongoDbClient(id: String): Any
}