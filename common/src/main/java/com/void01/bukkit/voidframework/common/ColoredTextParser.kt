package com.void01.bukkit.voidframework.common

import org.bukkit.ChatColor

object ColoredTextParser {
    @JvmOverloads
    fun parse(text: String, withRgb: Boolean = false): String {
        if (withRgb) {
            throw UnsupportedOperationException()
        }

        return ChatColor.translateAlternateColorCodes('&', text)
    }
}