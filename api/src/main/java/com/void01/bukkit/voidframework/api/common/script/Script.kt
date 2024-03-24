package com.void01.bukkit.voidframework.api.common.script

interface Script {
    val relativePath: String
    val clazz: Class<*>

    fun newInstance(): Any

    fun <T> newInstance(clazz: Class<T>): T
}