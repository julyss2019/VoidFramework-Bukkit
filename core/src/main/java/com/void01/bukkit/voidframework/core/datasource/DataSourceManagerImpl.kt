package com.void01.bukkit.voidframework.core.datasource

import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager
import com.void01.bukkit.voidframework.common.yaml.Yaml2
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.*
import javax.sql.DataSource

class DataSourceManagerImpl(private val plugin: VoidFrameworkPlugin) : DataSourceManager {
    private val logger = plugin.pluginLogger
    private val dataSourceMap = mutableMapOf<String, HikariDataSource>()
    private val dataSources get() = dataSourceMap.values

    init {
        load()
    }

    private fun load() {
        val yaml = Yaml2.from(plugin)
        val sharedDataSourcesSection = yaml.getSectionOrNull("shared-data-sources") ?: return

        sharedDataSourcesSection.getSubSections(false).forEach { sharedDataSourceSection ->
            val properties = Properties()

            sharedDataSourceSection.getKeys(false).forEach {
                properties[it] = sharedDataSourceSection.get(it)
            }

            val name = sharedDataSourceSection.name
            val dataSource = HikariDataSource(HikariConfig(properties))

            dataSourceMap[name] = dataSource
            logger.info("已加载 DataSource: $name")
        }
    }

    override fun getDataSource(id: String): DataSource = getDataSourceOrNull(id) ?: throw IllegalArgumentException("Unable to find SharedDataSource by id: $id")

    override fun getDataSourceOrNull(id: String): DataSource? {
        return dataSourceMap[id]
    }
}