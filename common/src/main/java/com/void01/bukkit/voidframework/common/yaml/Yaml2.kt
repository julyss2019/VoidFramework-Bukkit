package com.void01.bukkit.voidframework.common.yaml

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

class Yaml2(handle: ConfigurationSection) : Section2(handle) {
    companion object {
        @JvmStatic
        fun from(plugin: Plugin): Yaml2 {
            val file = File(plugin.dataFolder, "config.yml")

            return from(YamlConfiguration.loadConfiguration(file))
        }

        @JvmStatic
        fun from(file: File): Yaml2 {
            return from(YamlConfiguration.loadConfiguration(file))
        }

        @JvmStatic
        fun from(handle: YamlConfiguration): Yaml2 {
            return Yaml2(handle)
        }
    }
}