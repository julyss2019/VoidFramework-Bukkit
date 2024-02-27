package com.void01.bukkit.voidframework.core.groovy

import com.void01.bukkit.voidframework.api.common.groovy.GroovyClassLoader
import java.io.File

class GroovyClassLoaderImpl(private val reflectionHelper: GroovyClassLoaderReflectionHelper, val handle: Any) : GroovyClassLoader {
    override val loadedClasses: List<Class<*>>
        get() = reflectionHelper.getLoadedClasses(handle)

    override fun parseClass(file: File): Class<*> {
        return reflectionHelper.parseClass(handle, file)
    }

    override fun parseClass(text: String): Class<*> {
        return reflectionHelper.parseClass(handle, text)
    }

    override fun loadClass(name: String): Class<*>? {
        return reflectionHelper.loadClass(handle, name)
    }

    override fun clearCache() {
        reflectionHelper.clearCache(handle)
    }
}