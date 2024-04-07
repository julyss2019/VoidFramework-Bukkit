package com.void01.bukkit.voidframework.api.common.redission

import org.redisson.Redisson
import org.redisson.api.RedissonClient

interface RedissonManager {
    fun getSharedClientOrNull(id: String): RedissonClient?

    fun getSharedClient(id: String): RedissonClient
}