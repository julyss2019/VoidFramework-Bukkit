package com.void01.bukkit.voidframework.core.component

import com.void01.bukkit.voidframework.api.common.component.Component
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import kotlin.io.path.absolutePathString

class ComponentManagerImpl(private val plugin: VoidFrameworkPlugin) : ComponentManager {
    private val logger = plugin.pluginLogger
    private var libraryClassLoader: IsolatedClassLoader? = null
    private var componentClassLoaders = mutableListOf<IsolatedClassLoader>()

    init {
        load()
    }

    fun reload() {
        unloadComponents()
        load()
    }

    private fun load() {
        val pluginClassLoader = plugin.javaClass.classLoader

        libraryClassLoader = IsolatedClassLoader(pluginClassLoader)
        logger.info("Library ClassLoader: $libraryClassLoader")
        FileUtils.listFiles(plugin.componentLibsPath, "jar").forEach {
            libraryClassLoader!!.addURL(it.toFile())
            logger.info("已加载组件库: ${it.absolutePathString()}")
        }
        FileUtils.listFiles(plugin.componentsPath, "jar").forEach {
            val isolatedClassLoader = IsolatedClassLoader(object : ClassLoader() {
                override fun loadClass(name: String?): Class<*> {
                    return try {
                        libraryClassLoader!!.loadClass(name)
                    } catch (ex: Throwable) {
                        if (ex is ClassNotFoundException || ex is NoClassDefFoundError) {
                            pluginClassLoader.loadClass(name)
                        }

                        throw ex
                    }
                }
            })

            isolatedClassLoader.addURL(it.toFile())
            componentClassLoaders.add(isolatedClassLoader)
            logger.info("已加载组件: ${it.absolutePathString()}($isolatedClassLoader)")
        }
    }

    override fun getComponent(name: String): Component {
        return getComponentOrNull(name) ?: throw IllegalArgumentException("Unable to find Component by name: $name")
    }

    override fun getComponentOrNull(name: String): Component? {
        return componentClassLoaders
            .firstNotNullOfOrNull {
                try {
                    it.loadClass((name))
                } catch (ex: ClassNotFoundException) {
                    null
                }
            }
            ?.let {
                ComponentImpl(it)
            }
    }

    fun unloadComponents() {
        libraryClassLoader?.close()
        libraryClassLoader = null
        componentClassLoaders.forEach { it.close() }
        componentClassLoaders.clear()
        System.gc()
    }
}