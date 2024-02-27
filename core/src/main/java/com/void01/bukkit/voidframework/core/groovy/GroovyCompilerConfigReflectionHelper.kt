package com.void01.bukkit.voidframework.core.groovy

import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import java.util.*

class GroovyCompilerConfigReflectionHelper(isolatedClassLoader: IsolatedClassLoader) {
    private val compilerConfigClass = isolatedClassLoader.loadClass("org.codehaus.groovy.control.CompilerConfiguration")

    fun newInstance(): Any {
        return compilerConfigClass.newInstance()
    }

    fun configure(obj: Any, properties: Properties) {
        compilerConfigClass.getDeclaredMethod("configure", Properties::class.java).invoke(obj, properties)
    }

    fun setClassPaths(obj: Any, classPaths: List<String>) {
        compilerConfigClass.getDeclaredMethod("setClasspathList", List::class.java).invoke(obj, classPaths)
    }
}