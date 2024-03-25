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


    override fun getSharedClientOrNull(id: String): Any? {
        return mongoDbClientMap[id]
    }

    override fun getSharedClient(id: String): Any {
        return getSharedClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared MongoDB client by id: $id")
    }

    @Deprecated("弃用")
    override fun getSharedMongoDbClientOrNull(id: String): Any? {
        return getSharedClientOrNull(id)
    }

    @Deprecated("弃用")
    override fun getSharedMongoDbClient(id: String): Any {
        return getSharedClient(id)
    }

    fun closeAll() {
        mongoDbClientMap.values.forEach {
            it.close()
        }
    }
}