package com.void01.bukkit.voidframework.core.component

import com.void01.bukkit.voidframework.api.common.component.Component
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readBytes

class ComponentManagerImpl(private val plugin: VoidFrameworkPlugin) : ComponentManager {
    private val logger = plugin.pluginLogger
    private var componentClassLoader: IsolatedClassLoader? = null

    init {
        load()
    }

    fun reload() {
        componentClassLoader?.close()
        componentClassLoader = null
        System.gc()
        load()
    }

    private fun load() {
        componentClassLoader = IsolatedClassLoader(javaClass.classLoader)

        FileUtils.listFiles(plugin.componentLibsPath, "jar").forEach {
            componentClassLoader!!.addURL(copyTempFile(it))
            logger.info("已加载组件库: ${it.absolutePathString()}")
        }
        FileUtils.listFiles(plugin.componentsPath, "jar").forEach {
            componentClassLoader!!.addURL(copyTempFile(it))
            logger.info("已加载组件: ${it.absolutePathString()}")
        }
    }

    private fun copyTempFile(path: Path): Path {
        val tempFile = Files.createTempFile("void-framework-bukkit", "tmp")

        Files.write(tempFile, path.readBytes())
        tempFile.toFile().deleteOnExit()
        return tempFile
    }

    override fun getComponent(name: String): Component {
        return getComponentOrNull(name) ?: throw IllegalArgumentException("Unable to find Component by name: $name")
    }

    override fun getComponentOrNull(name: String): Component? {
        return try {
            componentClassLoader!!.loadClass(name)?.let {
                ComponentImpl(it)
            }
        } catch (ex: ClassNotFoundException) {
            null
        }
    }
}