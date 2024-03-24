package com.void01.bukkit.voidframework.core.datasource.shared

import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSource
import com.void01.bukkit.voidframework.api.common.datasource.shared.SharedDataSourceManager
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin

class SharedDataSourceManagerImpl(private val plugin: VoidFrameworkPlugin) : SharedDataSourceManager {
    private val typeClassMap = mutableMapOf<String, Class<out SharedDataSource>>()
    private val dataSourceMap = mutableMapOf<String, SharedDataSource>()

    init {
        registerType("hikari", HikariSharedDataSource::class.java)
        load()
    }

    private fun load() {
        val yaml = Yaml.fromPluginConfigFile(plugin)
        val dataSourcesSection = yaml.getSection("shared-data-sources", DefaultValue.of(null))

        dataSourcesSection?.subSections?.forEach {
            dataSourceMap[it.name] = getTypeClassOrThrow(it.getString("type")).newInstance()
                .apply {
                    val section = if (it.contains("config")) it.getSection("config") else it.getSection("properties")

                    loadProperties(section.bukkitSection)
                }
        }

        plugin.pluginLogger.info("载入了 ${dataSourceMap.size} 个共享连接池.")
    }

    fun reload() {
        closeDataSources()
    }

    fun registerType(type: String, clazz: Class<out SharedDataSource>) {
        require(!typeClassMap.containsKey(type)) {
            "Type already registered: $type(${clazz.name})"
        }

        typeClassMap[type] = clazz
    }

    fun getTypeClass(type: String): Class<out SharedDataSource>? {
        return typeClassMap[type]
    }

    fun getTypeClassOrThrow(type: String): Class<out SharedDataSource> {
        return getTypeClass(type) ?: throw RuntimeException("Unable to find type: $type")
    }

    fun closeDataSources() {
        closeDataSources()
        dataSourceMap.clear()
    }

    fun getDataSources(): List<SharedDataSource> {
        return dataSourceMap.values.toList()
    }

    override fun getDataSource(id: String): SharedDataSource? {
        return dataSourceMap[id]
    }
}