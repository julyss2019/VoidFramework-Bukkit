package com.void01.bukkit.voidframework.api.common.groovy

import java.io.File

interface GroovyManager {
    fun eval(scriptText: String) : Any?

    /**
     * 执行脚本
     */
    fun eval(scriptText: String, config: GroovyConfig, binding: GroovyBinding): Any?

    fun parseClass(sourceText: String): Class<*>

    /**
     * 解析类
     */
    fun parseClass(sourceText: String, config: GroovyConfig): Class<*>

    fun parseClass(sourceFile: File): Class<*>

    /**
     * 解析类
     * 使用 UTF-8 编码读取文件
     */
    fun parseClass(sourceFile: File, config: GroovyConfig): Class<*>
}