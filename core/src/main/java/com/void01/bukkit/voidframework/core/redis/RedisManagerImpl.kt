package com.void01.bukkit.voidframework.core.redis

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Section
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.void01.bukkit.voidframework.api.common.redis.RedisManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import redis.clients.jedis.Jedis

class RedisManagerImpl(plugin: VoidFrameworkPlugin) : RedisManager {
    private val jedisClientMap = mutableMapOf<String, Jedis>()

    init {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val clientsSection: Section? = yaml.getSection("redis-clients", DefaultValue.of(null))

        clientsSection?.subSections?.forEach {
            try {
                val clientInst = Jedis(it.getString("url"))

                clientInst.connect()
                jedisClientMap[it.name] = clientInst
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        plugin.pluginLogger.info("载入了 ${jedisClientMap.size} 个共享 Redis 客户端.")
    }

    override fun getSharedRedisClientOrNull(id: String): Any? {
        return jedisClientMap[id]
    }

    override fun getSharedRedisClient(id: String): Any {
        return getSharedRedisClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared redis client by id: $id")
    }
}