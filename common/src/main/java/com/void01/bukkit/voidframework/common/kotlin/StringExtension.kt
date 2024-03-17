package com.void01.bukkit.voidframework.common.kotlin

import com.github.julyss2019.bukkit.voidframework.text.Texts

fun String.toColored(): String {
    return Texts.getColoredText(this)
}