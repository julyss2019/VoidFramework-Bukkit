package com.void01.bukkit.voidframework.common

import org.bukkit.Bukkit
import java.util.UUID

/**
 * 玩家工具类
 * 提供了对玩家的一系列操作
 */
object PlayerUtils {
    /**
     * 判断玩家是否在线
     */
    fun isOnline(playerUUID: UUID): Boolean {
        return Bukkit.getPlayer(playerUUID) != null
    }

    /**
     * 判断玩家是否在线
     */
    fun isOnline(playerName: String): Boolean {
        // Bukkit 似乎有个 Bug，July_ss 在线，July 不在线，但仍然会获取到 July_ss
        return Bukkit.getPlayer(playerName).let {
            if (it != null) {
                it.name == playerName
            } else {
                false
            }
        }
    }
}