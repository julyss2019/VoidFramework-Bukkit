package com.void01.bukkit.voidframework.core.mongodb

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.void01.bukkit.voidframework.api.common.mongodb.MongoDbManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin

class MongoDbManagerImpl(private val plugin: VoidFrameworkPlugin) : MongoDbManager {
    private var mongoDbV4_11_1ClientMap = mutableMapOf<String, Any>()

    init {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val clientsSection = yaml.getSection("mongodb-clients", DefaultValue.of(null))

        if (clientsSection != null) {
            clientsSection.subSections.forEach {
                try {
                    val clientInst =
                        Class.forName("com.mongodb.v4_11_1.client.MongoClients")
                            .getDeclaredMethod("create", String::class.java)
                            .invoke(null, it.getString("url"))

                    mongoDbV4_11_1ClientMap[it.name] = clientInst!!
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        plugin.voidLogger.info("载入了 ${mongoDbV4_11_1ClientMap.size} 个共享 MongoDB 客户端.")
    }

    override fun getMongoDbClientV4_11_1(id: String): Any? {
        return mongoDbV4_11_1ClientMap[id]
    }

    fun closeAll() {
        mongoDbV4_11_1ClientMap.values.forEach {
            it.javaClass.getDeclaredMethod("close").invoke(it)
        }
    }
}