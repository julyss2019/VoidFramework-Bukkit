package com.void01.bukkit.voidframework.api.common.groovy

import java.io.File

interface GroovyClassLoader {
    val loadedClasses: List<Class<*>>

    fun parseClass(file: File): Class<*>

    fun parseClass(text: String): Class<*>

    fun loadClass(name: String): Class<*>?

    fun clearCache()
}