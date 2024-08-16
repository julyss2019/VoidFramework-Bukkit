package com.void01.bukkit.voidframework.api.common.logger

class JsonLog {
    private val internalKeyValueMap = mutableMapOf<String, Any>()
    val keyValueMap get() = internalKeyValueMap.toMap()

    fun put(key: String, value: Any) : JsonLog {
        internalKeyValueMap[key] = value
        return this
    }
}