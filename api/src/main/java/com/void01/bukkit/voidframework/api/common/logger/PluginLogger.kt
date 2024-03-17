package com.void01.bukkit.voidframework.api.common.logger

import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class PluginLogger(val plugin: Plugin) {
    var level: Level = Level.INFO

    fun log(level: Level, message: String) {
        if (level.intLevel >= this.level.intLevel) {
            Bukkit.getConsoleSender().sendMessage("${level.color}[${plugin.name}] [${level.name}] $message".toColored())
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