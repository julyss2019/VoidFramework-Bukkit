package com.void01.bukkit.voidframework.api.common.redission

interface RedissonManager {
    fun getSharedClientOrNull(id: String): Any?

    fun getSharedClient(id: String): Any
}