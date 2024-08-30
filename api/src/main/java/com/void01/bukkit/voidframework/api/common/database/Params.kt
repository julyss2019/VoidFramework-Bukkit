package com.void01.bukkit.voidframework.api.common.database

class Params(val params: List<Any> = emptyList())

fun params(vararg params: Any): Params {
    return Params(params.toList())
}