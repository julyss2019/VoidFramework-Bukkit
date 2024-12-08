package com.void01.bukkit.voidframework.common

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemUtils {
    fun setLores(itemStack: ItemStack, lores: List<String>): ItemStack {
        require(isValid(itemStack)) { "Invalid ItemStack" }

        val clone = itemStack.clone()
        val cloneMeta = clone.itemMeta

        cloneMeta.lore = lores
        clone.itemMeta = cloneMeta
        return clone
    }

    fun containsLore(item: ItemStack?, lore: String): Boolean {
        if (!isValid(item)) {
            return false
        }

        return getLores(item).contains(lore)
    }

    fun getLores(item: ItemStack?): MutableList<String> {
        if (!isValid(item)) {
            return mutableListOf()
        }

        return item!!.itemMeta?.lore ?: mutableListOf()
    }

    fun isValid(item: ItemStack?): Boolean {
        return item != null && item.type != Material.AIR
    }

    fun getDisplayName(item: ItemStack?): String? {
        if (!isValid(item)) {
            return null
        }

        return item!!.itemMeta?.displayName
    }
}