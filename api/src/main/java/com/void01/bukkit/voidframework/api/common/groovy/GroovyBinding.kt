package com.void01.bukkit.voidframework.api.common.groovy

class GroovyBinding {
    private val variableMap = mutableMapOf<String, Any>()

    fun setVariable(key: String, value: Any) {
        variableMap[key] = value
    }

    fun getVariables(): Map<String, Any> {
        return variableMap.toMap()
    }
}