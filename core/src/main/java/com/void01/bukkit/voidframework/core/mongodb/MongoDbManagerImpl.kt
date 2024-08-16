package com.void01.bukkit.voidframework.core.mongodb

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin

@Suppress("OVERRIDE_DEPRECATION")
class MongoDbManagerImpl(plugin: VoidFrameworkPlugin) : MongoDbManager {
    private val logger = plugin.pluginLogger
    private var mongoClientMap = mutableMapOf<String, MongoClient>()

    init {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val clientsSection = yaml.getSection("mongodb-clients", DefaultValue.of(null))

        @Suppress("IfThenToSafeAccess")
        if (clientsSection != null) {
            clientsSection.subSections.forEach {
                try {
                    val clientInst = MongoClients.create(it.getString("url"))

                    mongoClientMap[it.name] = clientInst!!
                    logger.info("已加载 MongoDB 客户端: ${it.name}")
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
        return mongoClientMap[id]
    }

    override fun getSharedClient(id: String): Any {
        return getSharedClient2(id)
    }

    override fun getSharedClientOrNull(id: String): Any? {
        return getSharedClientOrNull2(id)
    }

    override fun getSharedClientOrNull2(id: String): MongoClient? {
        return getSharedMongoClientOrNull(id)
    }

    override fun getSharedClient2(id: String): MongoClient {
        return getSharedMongoClient(id)
    }

    override fun getSharedMongoClientOrNull(id: String): MongoClient? {
        return mongoClientMap[id]
    }

    override fun getSharedMongoClient(id: String): MongoClient {
        return getSharedMongoClientOrNull(id)
            ?: throw IllegalArgumentException("Unable to find shared mongo client by id: $id")
    }

    override fun getSharedMongoDbClientOrNull(id: String): Any? {
        return getSharedClientOrNull(id)
    }

    override fun getSharedMongoDbClient(id: String): Any {
        return getSharedClient(id)
    }

    fun closeAll() {
        mongoClientMap.values.forEach {
            it.close()
        }
    }
}