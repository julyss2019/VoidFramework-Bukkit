package com.void01.bukkit.voidframework.common

fun noRelocate(expression: String): String {
    if (expression.length <= 2) {
        throw IllegalArgumentException("Illegal expressing")
    }

    return expression.substring(1, expression.length - 1)
}