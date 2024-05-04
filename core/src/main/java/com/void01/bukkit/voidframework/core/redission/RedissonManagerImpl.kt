@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.core.redission

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Section
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.void01.bukkit.voidframework.api.common.redission.RedissonManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

class RedissonManagerImpl(plugin: VoidFrameworkPlugin) : RedissonManager {
    private val logger = plugin.pluginLogger
    private val jedisClientMap = mutableMapOf<String, RedissonClient>()

    init {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val clientsSection: Section? = yaml.getSection("redis-clients", DefaultValue.of(null))

        clientsSection?.subSections?.forEach {
            try {
                val config = Config()

                config.useSingleServer().apply {
                    address = it.getString("url")
                }

                val id = it.name
                val clientInst = Redisson.create(config)

                jedisClientMap[id] = clientInst
                logger.info("已加载 Redisson 客户端: $id")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @Deprecated("命名优化")
    override fun getSharedClientOrNull(id: String): RedissonClient? {
        return jedisClientMap[id]
    }

    @Deprecated("命名优化")
    override fun getSharedClient(id: String): RedissonClient {
        return getSharedClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared RedissonClient by id: $id")
    }

    override fun getClient(id: String): RedissonClient {
        return getClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared RedissonClient by id: $id")
    }

    override fun getClientOrNull(id: String): RedissonClient? {
        return jedisClientMap[id]
    }
}