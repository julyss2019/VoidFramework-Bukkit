package com.void01.bukkit.voidframework.common.kotlin

fun <K, V> Map<K, V>.getOrThrow(key: K): V {
    require(containsKey(key)) {
        "Unable to find key '$key' in Map"
    }

    return get(key)!!
}