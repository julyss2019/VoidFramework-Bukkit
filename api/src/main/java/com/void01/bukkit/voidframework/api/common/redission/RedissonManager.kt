package com.void01.bukkit.voidframework.api.common.redission

import org.redisson.api.RedissonClient

interface RedissonManager {
    @Deprecated("命名优化")
    fun getSharedClientOrNull(id: String): RedissonClient?

    @Deprecated("命名优化")
    fun getSharedClient(id: String): RedissonClient

    fun getClient(id: String): RedissonClient

    fun getClientOrNull(id: String): RedissonClient?
}