package com.void01.bukkit.voidframework.common.kotlin

inline fun <reified T : Enum<*>> enumValueOfOrNull(name: String): T? =
    T::class.java.enumConstants.firstOrNull { it.name == name }