package com.void01.bukkit.voidframework.common.kotlin

import com.void01.bukkit.voidframework.common.SafeShadow

@Deprecated("弃用")
fun safeShadow(expression: String): String {
    return SafeShadow.parse(expression)
}