package com.void01.bukkit.voidframework.api.common.groovy

class GroovyCompilerConfig {
    var sourceEncoding: String = "UTF-8"
    private val classPaths = mutableSetOf<String>()

    fun addClasspath(classPath: String) {
        classPaths.add(classPath)
    }

    fun getClassPaths(): List<String> {
        return classPaths.toList()
    }
}