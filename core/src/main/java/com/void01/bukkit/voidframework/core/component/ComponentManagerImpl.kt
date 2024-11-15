package com.void01.bukkit.voidframework.core.component

import com.void01.bukkit.voidframework.api.common.component.Component
import com.void01.bukkit.voidframework.api.common.component.ComponentManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readBytes

class ComponentManagerImpl(private val plugin: VoidFrameworkPlugin) : ComponentManager {
    private val logger = plugin.pluginLogger
    private var componentClassLoader: IsolatedClassLoader? = null

    init {
        load()
    }

    fun reload() {
        // componentClassLoader?.close()
        componentClassLoader = null
        System.gc()
        load()
    }

    private fun load() {
        val isolatedClassLoader = IsolatedClassLoader(javaClass.classLoader)
        val componentLibFiles = FileUtils.listFiles(plugin.componentLibsPath, "jar")
        val componentFiles = FileUtils.listFiles(plugin.componentsPath, "jar")

        componentLibFiles.forEach {
            isolatedClassLoader.addURL(copyTempFile(it))
        }
        componentFiles.forEach {
            isolatedClassLoader.addURL(copyTempFile(it))
        }
        componentClassLoader = isolatedClassLoader
        logger.info("${componentLibFiles.size} component lib(s) loaded")
        logger.info("${componentFiles.size} component(s) loaded")
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