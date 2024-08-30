package com.void01.bukkit.voidframework.common

@Deprecated("弃用")
object SafeShadow {
    fun parse(expression: String): String {
        return expression.substring(1, expression.length - 1)
    }
}