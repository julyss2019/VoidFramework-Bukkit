package com.github.julyss2019.bukkit.voidframework.test.mock

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import org.mockito.Mockito.spy

class MockItemMeta : ItemMeta {
    companion object {
        fun mock(): MockItemMeta {
            return spy(MockItemMeta())
        }
    }

    private var _displayName: String? = null
    private var _lores: List<String> = emptyList()

    override fun clone(): ItemMeta {
        TODO("Not yet implemented")
    }

    override fun serialize(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun hasDisplayName(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String? {
        return _displayName
    }

    override fun setDisplayName(name: String?) {
        this._displayName = displayName
    }

    override fun hasLocalizedName(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLocalizedName(): String {
        TODO("Not yet implemented")
    }

    override fun setLocalizedName(name: String?) {
        TODO("Not yet implemented")
    }

    override fun hasLore(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLore(): List<String> {
        return _lores
    }

    override fun setLore(lore: List<String>) {
        this._lores = lore
    }

    override fun hasEnchants(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasEnchant(ench: Enchantment?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEnchantLevel(ench: Enchantment?): Int {
        TODO("Not yet implemented")
    }

    override fun getEnchants(): MutableMap<Enchantment, Int> {
        TODO("Not yet implemented")
    }

    override fun addEnchant(ench: Enchantment?, level: Int, ignoreLevelRestriction: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeEnchant(ench: Enchantment?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasConflictingEnchant(ench: Enchantment?): Boolean {
        TODO("Not yet implemented")
    }

    override fun addItemFlags(vararg itemFlags: ItemFlag?) {
        TODO("Not yet implemented")
    }

    override fun removeItemFlags(vararg itemFlags: ItemFlag?) {
        TODO("Not yet implemented")
    }

    override fun getItemFlags(): MutableSet<ItemFlag> {
        TODO("Not yet implemented")
    }

    override fun hasItemFlag(flag: ItemFlag?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isUnbreakable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setUnbreakable(unbreakable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MockItemMeta

        if (_displayName != other._displayName) return false
        if (_lores != other._lores) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _displayName?.hashCode() ?: 0
        result = 31 * result + _lores.hashCode()
        return result
    }

    override fun toString(): String {
        return "MockItemMeta(_displayName=$_displayName, _lores=$_lores)"
    }
}