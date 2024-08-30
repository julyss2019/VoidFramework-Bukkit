package com.void01.bukkit.voidframework.common.kotlin

@Deprecated("弃用")
fun <K, V> Map<K, V>.getOrThrow(key: K): V {
    require(containsKey(key)) {
        "Unable to find key '$key' in Map"
    }

    return get(key)!!
}