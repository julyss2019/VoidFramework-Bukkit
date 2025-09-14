package com.void01.bukkit.voidframework.commons.extension

import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer
import com.github.julyss2019.bukkit.voidframework.text.Texts
import org.bukkit.entity.Player

fun String.colored(): String {
    return Texts.getColoredText(this)
}

fun String.replacePlaceholders(vararg placeholders: Pair<String, Any>) : String {
    val placeholderContainer = PlaceholderContainer()

    placeholders.forEach {
        placeholderContainer.put(it.first, it.second)
    }

    return Texts.setPlaceholders(this, placeholderContainer)
}

fun String.setPlaceholders(player : Player) : String? {
    return null
}