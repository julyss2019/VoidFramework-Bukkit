package com.void01.bukkit.voidframework.api.common.extension

import com.github.julyss2019.bukkit.voidframework.VoidFramework
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger
import com.void01.bukkit.voidframework.api.common.logger.PluginLogger
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.common.JarScanner2
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream

open class VoidPlugin : JavaPlugin() {
    lateinit var pluginLogger: PluginLogger
        private set

    override fun onEnable() {
        pluginLogger = PluginLogger(this)
        onPluginEnable()
    }

    override fun onDisable() {
        onPluginDisable()
        VoidFramework.getCommandManager().unregisterCommandFrameworks(this)
        VoidFramework.getLogManager().unregisterLoggers(this)
    }

    fun saveDefaults() {
        val jarScanner2 = JarScanner2(javaClass, listOf("/defaults/**"))

        jarScanner2.scan(object : JarScanner2.ScanResultHandler {
            override fun handle(path: String, inputStream: InputStream) {
                FileUtils.writeInputStream(inputStream, File(dataFolder, path.substring("/defaults".length)), false)
            }
        })
    }

    open fun onPluginEnable() {
    }

    open fun onPluginDisable() {
    }
}