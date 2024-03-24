package com.void01.bukkit.voidframework.api.common.redis

interface RedisManager {
    fun getSharedRedisClientOrNull(id: String): Any?

    fun getSharedRedisClient(id: String): Any
}