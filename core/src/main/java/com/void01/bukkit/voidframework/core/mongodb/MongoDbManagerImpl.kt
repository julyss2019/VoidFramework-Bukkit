package com.void01.bukkit.voidframework.core.mongodb

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClientFactory
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
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        logger.info("载入了 ${mongoDbClientMap.size} 个共享 MongoDB 客户端.")
        mongoDbClientMap.keys.forEach {
            logger.info("- $it")
        }
    }

    @Deprecated("返回类型不明确", ReplaceWith("getSharedClient2(id)"))
    override fun getSharedClient(id: String): Any {
        return getSharedClient2(id)
    }

    @Deprecated("返回类型不明确", ReplaceWith("getSharedClientOrNull2(id)"))
    override fun getSharedClientOrNull(id: String): Any? {
        return getSharedClientOrNull2(id)
    }

    override fun getSharedClientOrNull2(id: String): MongoClient? {
        return mongoDbClientMap[id]
    }

    override fun getSharedClient2(id: String): MongoClient {
        return getSharedClientOrNull2(id) ?: throw IllegalArgumentException("Unable to find shared MongoDB client by id: $id")
    }

    @Deprecated("弃用", ReplaceWith("getSharedClientOrNull(id)"))
    override fun getSharedMongoDbClientOrNull(id: String): Any? {
        return getSharedClientOrNull(id)
    }

    @Deprecated("弃用", ReplaceWith("getSharedClient(id)"))
    override fun getSharedMongoDbClient(id: String): Any {
        return getSharedClient(id)
    }

    fun closeAll() {
        mongoDbClientMap.values.forEach {
            it.close()
        }
    }
}