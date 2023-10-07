package com.void01.bukkit.voidframework.common.kotlin

import com.void01.bukkit.voidframework.common.ColoredTextParser

@JvmOverloads
fun String.toColored(withRgb: Boolean = false): String {
    return ColoredTextParser.parse(this)
}