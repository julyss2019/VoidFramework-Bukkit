package com.void01.bukkit.voidframework.api.common.redis

interface RedisManager {
    fun getSharedClientOrNull(id: String): Any?

    fun getSharedClient(id: String): Any
}