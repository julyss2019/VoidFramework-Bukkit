package com.void01.bukkit.voidframework.api.common.mongodb

import com.mongodb.client.MongoClient

interface MongoDbManager {
    @Deprecated(message = "弃用")
    fun getSharedMongoDbClientOrNull(id: String): Any?

    @Deprecated(message = "弃用")
    fun getSharedMongoDbClient(id: String): Any

    @Deprecated(message = "返回类型不明确")
    fun getSharedClientOrNull(id: String): Any?

    @Deprecated(message = "返回类型不明确")
    fun getSharedClient(id: String): Any

    fun getSharedClientOrNull2(id: String): MongoClient?

    fun getSharedClient2(id: String): MongoClient
}