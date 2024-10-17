package com.void01.bukkit.voidframework.api.common.logger

import com.google.gson.GsonBuilder
import com.void01.bukkit.voidframework.api.common.logger.gson.ItemStackTypeAdapter
import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class PluginLogger(val plugin: Plugin) {
    companion object {
        val GSON = GsonBuilder()
            .registerTypeHierarchyAdapter(ItemStack::class.java, ItemStackTypeAdapter())
            .create()
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

    fun debug(jsonLog: JsonLog) {
        log(Level.DEBUG, jsonLog)
    }

    fun debug(message: String, jsonLog: JsonLog) {
        debug(mergeMessageJsonLog(message, jsonLog))
    }

    fun info(jsonLog: JsonLog) {
        log(Level.INFO, jsonLog)
    }

    fun info(message: String, jsonLog: JsonLog) {
        info(mergeMessageJsonLog(message, jsonLog))
    }

    fun warn(jsonLog: JsonLog) {
        log(Level.WARN, jsonLog)
    }

    fun warn(message: String, jsonLog: JsonLog) {
        warn(mergeMessageJsonLog(message, jsonLog))
    }

    fun error(jsonLog: JsonLog) {
        log(Level.ERROR, jsonLog)
    }

    fun error(message: String, jsonLog: JsonLog) {
        error(mergeMessageJsonLog(message, jsonLog))
    }

    /**
     * 合并消息到 JsonLog
     */
    private fun mergeMessageJsonLog(message: String, jsonLog: JsonLog): JsonLog {
        val newJsonLog = JsonLog().put("message", message)

        return newJsonLog.apply {
            jsonLog.keyValueMap.forEach {
                put(it.key, it.value)
            }
        }
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

    fun log(threshold: Level, jsonLog: JsonLog) {
        log(threshold, GSON.toJson(jsonLog.keyValueMap))
    }

    fun log(threshold: Level, message: String) {
        if (threshold.intLevel >= this.threshold.intLevel) {
            Bukkit.getConsoleSender()
                .sendMessage("${threshold.color}[${plugin.name}] [${threshold.name}] $message".toColored())
        }
    }
}