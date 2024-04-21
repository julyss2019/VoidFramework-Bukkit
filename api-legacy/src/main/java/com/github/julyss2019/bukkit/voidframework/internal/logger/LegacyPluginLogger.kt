package com.github.julyss2019.bukkit.voidframework.internal.logger

import com.github.julyss2019.bukkit.voidframework.text.Texts
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class LegacyPluginLogger(val plugin: Plugin) {
    var threshold: Level = Level.INFO

    fun log(level: Level, message: String) {
        if (level.intLevel >= this.threshold.intLevel) {
            Bukkit.getConsoleSender().sendMessage(Texts.getColoredText("${level.color}[${plugin.name}] [${level.name}] $message"))
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