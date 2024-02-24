package com.void01.bukkit.voidframework.common

import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemBuilder {
    private var material: Material = Material.STONE
    private var amount: Int = 1
    private var subId: Short = 0
    private var displayName: String? = null
    private var lores = mutableListOf<String>()
    private var colored: Boolean = true

    fun setMaterial(material: Material): ItemBuilder {
        require(material != Material.AIR) {
            "material is not allow to be AIR"
        }

        this.material = material
        return this
    }

    fun setSubId(subId: Short): ItemBuilder {
        this.subId = subId
        return this
    }

    fun setDisplayName(displayName: String): ItemBuilder {
        this.displayName = displayName
        return this
    }

    fun setLores(vararg lores: String): ItemBuilder {
        this.lores = lores.toMutableList()
        return this
    }

    fun setLores(lores: List<String>): ItemBuilder {
        this.lores = lores.toMutableList()
        return this
    }

    fun appendLores(vararg lores: String): ItemBuilder {
        this.lores.addAll(lores)
        return this
    }

    fun appendLore(lore: String): ItemBuilder {
        this.lores.add(lore)
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        require(amount >= 1) {
            "amount must be greater than or equal to 1"
        }

        this.amount = amount
        return this
    }

    fun toColored(): ItemBuilder {
        this.colored = true
        return this
    }

    fun toRgbColored(): ItemBuilder {
        throw UnsupportedOperationException()
        return this
    }

    fun build(): ItemStack {
        val bukkitItem = ItemStack(material)

        require(bukkitItem.amount <= bukkitItem.maxStackSize) {
            "amount must be less than or equal to ${bukkitItem.maxStackSize}"
        }

        bukkitItem.amount = amount
        bukkitItem.itemMeta = bukkitItem.itemMeta!!.also {
            var processedDisplayName = displayName

            if (colored) {
                processedDisplayName = processedDisplayName?.toColored()
            }

            it.setDisplayName(processedDisplayName)
            it.lore = lores
                .map { lore ->
                    if (colored) {
                        lore.toColored()
                    } else {
                        lore
                    }
                }
        }

        return bukkitItem
    }
}