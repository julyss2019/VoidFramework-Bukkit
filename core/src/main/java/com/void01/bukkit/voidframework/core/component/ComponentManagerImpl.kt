package com.void01.bukkit.voidframework.core.component

import com.void01.bukkit.voidframework.api.common.component.Component
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import kotlin.io.path.absolutePathString

class ComponentManagerImpl(private val plugin: VoidFrameworkPlugin) : ComponentManager {
    private val logger = plugin.pluginLogger
    private lateinit var componentClassLoader: IsolatedClassLoader

    init {
        load()
    }

    fun reload() {
        load()
    }

    private fun load() {
        componentClassLoader = IsolatedClassLoader(plugin.javaClass.classLoader)

        FileUtils.listFiles(plugin.componentLibsPath, "jar").forEach {
            componentClassLoader.addURL(it.toFile())
            logger.info("已加载组件库: ${it.absolutePathString()}")
        }
        FileUtils.listFiles(plugin.componentsPath, "jar").forEach {
            componentClassLoader.addURL(it.toFile())
            logger.info("已加载组件: ${it.absolutePathString()}")
        }
    }

    override fun getComponent(name: String): Component {
        return getComponentOrNull(name) ?: throw IllegalArgumentException("Unable to find Component by name: $name")
    }

    override fun getComponentOrNull(name: String): Component? {
        return try {
            componentClassLoader.loadClass(name)?.let {
                ComponentImpl(it)
            }
        } catch (ex: ClassNotFoundException) {
            null
        }
    }
}