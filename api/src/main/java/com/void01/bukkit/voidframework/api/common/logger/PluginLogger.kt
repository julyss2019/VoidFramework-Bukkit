package com.void01.bukkit.voidframework.api.common.logger

import com.google.gson.Gson
import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class PluginLogger(val plugin: Plugin) {
    companion object {
        val GSON = Gson()
    }

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

    fun log(threshold: Level, jsonLog: JsonLog) {
        log(threshold, GSON.toJson(jsonLog.keyValueMap))
    }

    fun log(threshold: Level, message: String) {
        if (threshold.intLevel >= this.threshold.intLevel) {
            Bukkit.getConsoleSender()
                .sendMessage("${threshold.color}[${plugin.name}] [${threshold.name}] $message".toColored())
        }
    }

    fun info(jsonLog: JsonLog) {
        log(Level.INFO, jsonLog)
    }

    fun info(message: String, jsonLog: JsonLog) {
        info(jsonLog.put("message", message))
    }

    fun debug(jsonLog: JsonLog) {
        log(Level.DEBUG, jsonLog)
    }

    fun debug(message: String, jsonLog: JsonLog) {
        debug(jsonLog.put("message", message))
    }

    fun warn(jsonLog: JsonLog) {
        log(Level.WARN, jsonLog)
    }

    fun warn(message: String, jsonLog: JsonLog) {
        warn(jsonLog.put("message", message))
    }

    fun error(jsonLog: JsonLog) {
        log(Level.ERROR, jsonLog)
    }

    fun error(message: String, jsonLog: JsonLog) {
        error(jsonLog.put("message", message))
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