package com.void01.bukkit.voidframework.common.kotlin

import org.bukkit.entity.Entity

fun Entity.getNameWithUuid(): String {
    return "${this.name}(${this.uniqueId})"
}