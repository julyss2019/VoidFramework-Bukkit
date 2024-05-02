package com.void01.bukkit.voidframework.common.kotlin

import org.bukkit.entity.Player

fun Player.getNameWithUuid(): String {
    return "${this.name}(${this.uniqueId})"
}

val Player.nameWithUuid2 get() = "${this.name}(${this.uniqueId})"