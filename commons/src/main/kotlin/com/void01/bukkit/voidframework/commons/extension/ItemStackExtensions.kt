package com.void01.bukkit.voidframework.commons.extension

import com.void01.bukkit.voidframework.commons.util.checkValidItemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

val ItemStack?.lores : List<String>
    get() {
        if (isValid) {
            return emptyList()
        }

        return this?.itemMeta?.lore ?: emptyList()
    }

val ItemStack?.displayName : String?
    get()  {
        if (isValid) {
            return null
        }

        return this?.itemMeta?.displayName
    }

val ItemStack?.isValid: Boolean
    get() {
        return this != null && this.type != Material.AIR
    }

fun ItemStack.setDisplayName(displayName: String?) {
    checkValidItemStack(this)

    val newItemMeta = itemMeta

    newItemMeta.displayName = displayName
    itemMeta = newItemMeta
}

fun ItemStack.setLores(lores : List<String>) {
    checkValidItemStack(this)

    val newItemMeta = itemMeta

    newItemMeta.lore = lores
    itemMeta = newItemMeta
}

fun ItemStack.setLores(vararg lores: String) {
    setLores(lores.toList())
}