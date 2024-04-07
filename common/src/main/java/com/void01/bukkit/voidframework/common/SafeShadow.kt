package com.void01.bukkit.voidframework.common

object SafeShadow {
    fun parse(expression: String): String {
        return expression.substring(1, expression.length - 1)
    }
}