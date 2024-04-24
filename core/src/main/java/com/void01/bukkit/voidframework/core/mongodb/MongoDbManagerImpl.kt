@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.core.mongodb

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin

class MongoDbManagerImpl(plugin: VoidFrameworkPlugin) : MongoDbManager {
    private val logger = plugin.pluginLogger
    private var mongoDbClientMap = mutableMapOf<String, MongoClient>()

    init {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val clientsSection = yaml.getSection("mongodb-clients", DefaultValue.of(null))

        @Suppress("IfThenToSafeAccess")
        if (clientsSection != null) {
            clientsSection.subSections.forEach {
                try {
                    val clientInst = MongoClients.create(it.getString("url"))

                    mongoDbClientMap[it.name] = clientInst!!
                    logger.info("已载入 MongoDB 客户端: ${it.name}")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun getClient(id: String): MongoClient {
        return getClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared MongoDB client by id: $id")
    }

    override fun getClientOrNull(id: String): MongoClient? {
        return mongoDbClientMap[id]
    }

    @Deprecated("返回类型不明确")
    override fun getSharedClient(id: String): Any {
        return getSharedClient2(id)
    }

    @Deprecated("返回类型不明确")
    override fun getSharedClientOrNull(id: String): Any? {
        return getSharedClientOrNull2(id)
    }

    @Deprecated("命名优化")
    override fun getSharedClientOrNull2(id: String): MongoClient? {
        return mongoDbClientMap[id]
    }

    @Deprecated("命名优化")
    override fun getSharedClient2(id: String): MongoClient {
        return getSharedClientOrNull2(id) ?: throw IllegalArgumentException("Unable to find shared MongoDB client by id: $id")
    }

    @Deprecated("命名优化")
    override fun getSharedMongoDbClientOrNull(id: String): Any? {
        return getSharedClientOrNull(id)
    }

    @Deprecated("命名优化")
    override fun getSharedMongoDbClient(id: String): Any {
        return getSharedClient(id)
    }

    fun closeAll() {
        mongoDbClientMap.values.forEach {
            it.close()
        }
    }
}