package com.void01.bukkit.voidframework.common

import org.bukkit.Bukkit
import org.bukkit.event.Event

object BukkitUtils {
    fun callEvent(event: Event) {
        Bukkit.getPluginManager().callEvent(event)
    }
}