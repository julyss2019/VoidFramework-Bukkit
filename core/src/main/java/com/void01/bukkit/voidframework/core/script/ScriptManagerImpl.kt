package com.void01.bukkit.voidframework.core.script

import com.void01.bukkit.voidframework.api.common.VoidFramework3
import com.void01.bukkit.voidframework.api.common.groovy.GroovyCompilerConfig
import com.void01.bukkit.voidframework.api.common.script.Script
import com.void01.bukkit.voidframework.api.common.script.ScriptManager
import com.void01.bukkit.voidframework.common.FileUtils
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import java.lang.IllegalArgumentException
import java.nio.file.Files

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
        load()
    }

    fun load() {
        val scriptsDir = plugin.getScriptsDir()
        val groovyClassLoader = VoidFramework3.getGroovyManager().createClassLoader(
            plugin.javaClass.classLoader,
            GroovyCompilerConfig().apply {
                addClasspath(plugin.getScriptsDir().absolutePath)
            })

        if (scriptsDir.exists()) {
            Files
                .walk(scriptsDir.toPath())
                .use { walk ->
                    walk
                        .filter {
                            Files.isRegularFile(it)
                        }
                        .filter {
                            it.toString().endsWith(".groovy")
                        }
                        .forEach {
                            val scriptFile = it.toFile()
                            val relativePath = FileUtils.getRelativePath(file = scriptFile, parent = scriptsDir)

                            try {
                                scriptMap[relativePath] = ScriptImpl(relativePath, groovyClassLoader.parseClass(scriptFile))
                            } catch (ex: Exception) {
                                throw RuntimeException("在解析 ${scriptFile.absolutePath} 时发生了异常", ex)
                            }
                        }
                }
        }

        logger.info("载入了 " + scriptMap.size + " 个脚本: ")
        scripts.forEach {
            logger.info(it.toString())
        }
    }

    override fun getScript(relativePath: String): Script {
        return getScriptOrNull(relativePath) ?: throw IllegalArgumentException("Unable to find Script by relative path: $relativePath")
    }

    override fun getScriptOrNull(relativePath: String): Script? {
        return scriptMap[relativePath]
    }
}