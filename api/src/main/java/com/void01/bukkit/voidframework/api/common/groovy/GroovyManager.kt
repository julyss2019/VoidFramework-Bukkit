@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.api.common.groovy

import java.io.File

interface GroovyManager {
    @Deprecated("效率低下")
    fun eval(scriptText: String): Any?

    /**
     * 执行脚本
     */
    @Deprecated("效率低下")
    fun eval(scriptText: String, config: GroovyConfig, binding: GroovyBinding): Any?

    @Deprecated("使用 createClassLoader")
    fun parseClass(sourceText: String): Class<*>

    /**
     * 解析类
     */
    @Deprecated("使用 createClassLoader")
    fun parseClass(sourceText: String, config: GroovyConfig): Class<*>

    @Deprecated("使用 createClassLoader")
    fun parseClass(sourceFile: File): Class<*>

    /**
     * 解析类
     * 使用 UTF-8 编码读取文件
     */
    @Deprecated("使用 createClassLoader")
    fun parseClass(sourceFile: File, config: GroovyConfig): Class<*>

    fun createClassLoader(parentClassLoader: ClassLoader, compilerConfig: GroovyCompilerConfig): GroovyClassLoader
}