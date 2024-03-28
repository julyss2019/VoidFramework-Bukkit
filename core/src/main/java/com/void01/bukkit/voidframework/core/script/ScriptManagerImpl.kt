package com.void01.bukkit.voidframework.core.script

import com.void01.bukkit.voidframework.api.common.VoidFramework3
import com.void01.bukkit.voidframework.api.common.groovy.GroovyCompilerConfig
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.api.common.script.Script
import com.void01.bukkit.voidframework.api.common.script.ScriptManager
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import java.lang.IllegalArgumentException
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.relativeTo

class ScriptManagerImpl(private val plugin: VoidFrameworkPlugin) : ScriptManager {
    private val logger = plugin.pluginLogger
    private val scriptMap = mutableMapOf<String, Script>()
    override val scripts: List<Script>
        get() {
            return scriptMap.values.toList()
        }

    init {
        load()
    }

    fun reload() {
        scriptMap.clear()
        load()
    }

    private fun load() {
        val scriptsPath = plugin.scriptsPath
        val scriptLibsPath = plugin.scriptLibsPath
        val groovyCompilerConfig = GroovyCompilerConfig()
        val isolatedClassLoader = IsolatedClassLoader(plugin.javaClass.classLoader)

        groovyCompilerConfig.addClassPath(scriptsPath.absolutePathString())

        FileUtils.listFiles(scriptLibsPath, ".jar").forEach {
            isolatedClassLoader.addURL(it.toFile())
            logger.info("已载入脚本库: ${it.absolutePathString()}.")
        }

        val groovyClassLoader = VoidFramework3.getGroovyManager().createClassLoader(
            isolatedClassLoader,
            groovyCompilerConfig
        )

        FileUtils.listFiles(scriptsPath, ".groovy").forEach {
            val relativePath = "/" + it.relativeTo(scriptsPath).toString().replace("\\", "/")

            try {
                scriptMap[relativePath] = ScriptImpl(it, relativePath, groovyClassLoader.parseClass(it.toFile()))
            } catch (ex: Exception) {
                logger.warn("在解析脚本 ${it.absolutePathString()} 时发生了异常\n${ex.stackTraceToString()}")
            }

            logger.info("已载入脚本: ${it.absolutePathString()} [${relativePath}]")
        }
    }

    override fun getScript(relativePath: String): Script {
        return getScriptOrNull(relativePath) ?: throw IllegalArgumentException("Unable to find Script by relative path: $relativePath")
    }

    override fun getScriptOrNull(relativePath: String): Script? {
        var adjustedRelativePath = relativePath.replace("\\", "/")

        if (!adjustedRelativePath.startsWith("/")) {
            adjustedRelativePath = "/$adjustedRelativePath"
        }

        return scriptMap[adjustedRelativePath]
    }
}