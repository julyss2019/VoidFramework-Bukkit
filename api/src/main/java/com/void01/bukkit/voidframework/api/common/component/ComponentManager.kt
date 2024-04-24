package com.void01.bukkit.voidframework.api.common.component

interface ComponentManager {
    fun getComponent(name: String): Component

    fun getComponentOrNull(name: String): Component?
}