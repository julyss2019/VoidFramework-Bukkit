package com.void01.bukkit.voidframework.common.logger

import org.bukkit.ChatColor

enum class Level(val intLevel: Int, val color: ChatColor) {
    DEBUG(0, ChatColor.GRAY),
    INFO(1, ChatColor.WHITE),
    WARN(2, ChatColor.YELLOW),
    ERROR(3, ChatColor.RED)
}