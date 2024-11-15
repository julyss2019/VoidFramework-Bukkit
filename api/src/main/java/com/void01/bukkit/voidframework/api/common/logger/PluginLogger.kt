package com.void01.bukkit.voidframework.api.common.logger

import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class PluginLogger(val plugin: Plugin) {
    @Deprecated("命名优化")
    var threshold: Level
        get() = level
        set(value) {
            level = value
        }

    var level: Level = Level.INFO
        set(value) {
            field = value
            plugin.logger.info("Logger level: $value")
        }

    fun debug(message: String) {
        log(Level.DEBUG, message)
    }

    fun info(message: String) {
        log(Level.INFO, message)
    }

    fun warn(message: String) {
        log(Level.WARN, message)
    }

    fun error(message: String) {
        log(Level.ERROR, message)
    }

    fun log(threshold: Level, message: String) {
        if (threshold.intLevel >= this.level.intLevel) {
            Bukkit.getConsoleSender()
                .sendMessage("${threshold.color}[${plugin.name}] [${threshold.name}] $message".toColored())
        }
    }
}