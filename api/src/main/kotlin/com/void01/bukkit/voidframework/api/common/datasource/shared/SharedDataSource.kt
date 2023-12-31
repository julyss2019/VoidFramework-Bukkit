package com.void01.bukkit.voidframework.api.common.datasource.shared

import org.bukkit.configuration.ConfigurationSection
import javax.sql.DataSource

interface SharedDataSource : DataSource {
    fun loadConfig(section: ConfigurationSection)

    fun close()
}