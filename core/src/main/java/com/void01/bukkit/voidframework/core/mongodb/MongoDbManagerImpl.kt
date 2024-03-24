package com.void01.bukkit.voidframework.core.mongodb

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClientFactory
import com.mongodb.client.MongoClients
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin

class MongoDbManagerImpl(plugin: VoidFrameworkPlugin) : MongoDbManager {
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

        plugin.pluginLogger.info("载入了 ${mongoDbClientMap.size} 个共享 MongoDB 客户端.")
    }

    override fun getSharedMongoDbClientOrNull(id: String): Any? {
        return mongoDbClientMap[id]
    }

    override fun getSharedMongoDbClient(id: String): Any {
        return getSharedMongoDbClientOrNull(id) ?: throw IllegalArgumentException("Unable to find shared MongoDB client by id: $id")
    }

    fun closeAll() {
        mongoDbClientMap.values.forEach {
            it.close()
        }
    }
}