package com.void01.bukkit.voidframework.api.common.groovy

class GroovyCompilerConfig {
    var sourceEncoding: String = "UTF-8"
    private val classPaths = mutableSetOf<String>()

    @Deprecated(message = "大小写错误", replaceWith = ReplaceWith("this.addClassPath(classPath)"))
    fun addClasspath(classPath: String) {
        classPaths.add(classPath)
    }

    fun addClassPath(classPath: String) {
        classPaths.add(classPath)
    }

    fun getClassPaths(): List<String> {
        return classPaths.toList()
    }
}