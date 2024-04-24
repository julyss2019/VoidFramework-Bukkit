package com.void01.bukkit.voidframework.core.groovy

import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import java.io.File

class GroovyClassLoaderReflectionHelper(isolatedClassLoader: IsolatedClassLoader) {
    private val groovyClassLoaderClass = isolatedClassLoader.loadClass("groovy.lang.GroovyClassLoader")
    private val groovyCompilerConfigClass = isolatedClassLoader.loadClass("org.codehaus.groovy.control.CompilerConfiguration")

    fun newInstance(parentClassLoader: ClassLoader, originCompilerConfiguration: Any): Any {
        return groovyClassLoaderClass.getConstructor(ClassLoader::class.java, groovyCompilerConfigClass)
            .newInstance(parentClassLoader, originCompilerConfiguration)
    }

    fun clearCache(obj: Any) {
        groovyClassLoaderClass.getDeclaredMethod("clearCache").invoke(obj)
    }

    fun loadClass(obj: Any, name: String): Class<*>? {
        return groovyClassLoaderClass.getDeclaredMethod("loadClass", String::class.java).invoke(obj, name) as Class<*>?
    }

    fun getLoadedClasses(obj: Any): List<Class<*>> {
        @Suppress("UNCHECKED_CAST")
        return (groovyClassLoaderClass.getDeclaredMethod("getLoadedClasses").invoke(obj) as Array<Class<*>>).toList()
    }

    fun parseClass(obj: Any, text: String): Class<*> {
        return groovyClassLoaderClass.getDeclaredMethod("parseClass", String::class.java).invoke(obj, text) as Class<*>
    }

    fun parseClass(obj: Any, file: File): Class<*> {
        return groovyClassLoaderClass.getDeclaredMethod("parseClass", File::class.java).invoke(obj, file) as Class<*>
    }

    fun addClassPath(obj: Any, path: String) {
        groovyClassLoaderClass.getDeclaredMethod("addClasspath", String::class.java).invoke(obj, path)
    }
}