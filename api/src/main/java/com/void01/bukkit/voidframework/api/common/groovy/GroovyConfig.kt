@file:Suppress("DEPRECATION")

package com.void01.bukkit.voidframework.api.common.groovy

import com.void01.bukkit.voidframework.api.common.VoidFramework2

@Deprecated("弃用")
class GroovyConfig {
    var parentClassLoader : ClassLoader = VoidFramework2::class.java.classLoader
    var sourceEncoding: String = "UTF-8"
    private val classPaths = mutableSetOf<String>()

    fun addClasspath(classPath: String) {
        classPaths.add(classPath)
    }

    fun getClassPaths(): List<String> {
        return classPaths.toList()
    }
}