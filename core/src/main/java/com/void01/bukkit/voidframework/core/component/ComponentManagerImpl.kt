package com.void01.bukkit.voidframework.core.component

import com.void01.bukkit.voidframework.api.common.component.Component
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import kotlin.io.path.absolutePathString

class ComponentManagerImpl(private val plugin: VoidFrameworkPlugin) : ComponentManager {
    private val logger = plugin.pluginLogger
    private lateinit var isolatedClassLoader: IsolatedClassLoader

    init {
        load()
    }

    fun reload() {
        load()
    }

    private fun load() {
        isolatedClassLoader = IsolatedClassLoader(plugin.javaClass.classLoader)
        FileUtils.listFiles(plugin.componentsPath, "jar").forEach {
            isolatedClassLoader.addURL(it.toFile())
            logger.info("已载入组件: ${it.absolutePathString()}")
        }
        FileUtils.listFiles(plugin.componentLibsPath, "jar").forEach {
            isolatedClassLoader.addURL(it.toFile())
            logger.info("已载入组件库: ${it.absolutePathString()}")
        }
    }

    override fun getComponent(name: String): Component {
        return getComponentOrNull(name) ?: throw IllegalArgumentException("Unable to find Component by name: $name")
    }

    override fun getComponentOrNull(name: String): Component? {
        return isolatedClassLoader.loadClass(name)?.let {
            return ComponentImpl(it)
        }
    }
}