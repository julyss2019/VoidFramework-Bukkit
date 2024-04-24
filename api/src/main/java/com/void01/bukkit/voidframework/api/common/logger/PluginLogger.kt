package com.void01.bukkit.voidframework.api.common.logger

import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class PluginLogger(val plugin: Plugin) {
    @Deprecated("命名优化")
    var level: Level
        get() = threshold
        set(value) {
            threshold = value
        }

    var threshold: Level = Level.INFO
        set(value) {
            field = value
            plugin.logger.info("日志等级: $value")
        }

    fun log(threshold: Level, message: String) {
        if (threshold.intLevel >= this.threshold.intLevel) {
            Bukkit.getConsoleSender().sendMessage("${threshold.color}[${plugin.name}] [${threshold.name}] $message".toColored())
        }
    }

    fun info(message: String) {
        log(Level.INFO, message)
    }

    fun debug(message: String) {
        log(Level.DEBUG, message)
    }

    fun warn(message: String) {
        log(Level.WARN, message)
    }

    fun error(message: String) {
        log(Level.ERROR, message)
    }
}