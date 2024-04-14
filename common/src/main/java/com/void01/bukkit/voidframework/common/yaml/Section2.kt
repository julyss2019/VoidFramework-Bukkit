package com.void01.bukkit.voidframework.common.yaml

import com.github.julyss2019.bukkit.voidframework.common.Reflections
import com.void01.bukkit.voidframework.common.ItemBuilder
import com.void01.bukkit.voidframework.common.kotlin.toColored
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemoryConfiguration
import org.bukkit.inventory.ItemStack
import java.util.function.Supplier

open class Section2 protected constructor(val handle: ConfigurationSection) {
    companion object {
        fun emptySection(): Section2 = Section2(MemoryConfiguration())

        fun from(handle: ConfigurationSection): Section2 {
            return Section2(handle)
        }
    }

    val name: String get() = handle.name
    val currentPath: String get() = handle.currentPath
    val root: Section2
        get() {
            return Section2(handle.root)
        }
    val parent: Section2?
        get() {
            return Section2(handle.parent ?: return null)
        }

    fun getSubSections(deep: Boolean = false): List<Section2> {
        return getKeys(deep).map { Section2(handle.getConfigurationSection(it)) }
    }

    fun getKeys(deep: Boolean = false): List<String> {
        return handle.getKeys(deep).toList()
    }

    fun contains(path: String): Boolean {
        return handle.contains(path)
    }

    fun getSection(path: String): Section2 {
        return getOrThrow(path) {
            getSectionOrNull(path)
        }
    }

    fun getSectionOrNull(path: String): Section2? = getSectionOrDefault(path, null)

    fun getSectionOrDefault(path: String, def: Section2?): Section2? {
        return from(handle.getConfigurationSection(path) ?: return def)
    }

    fun getStringList(path: String): List<String> {
        return handle.getStringList(path) ?: emptyList()
    }

    fun getList(path: String): List<*> {
        return handle.getList(path) ?: emptyList<Any>()
    }

    fun getBoolean(path: String): Boolean {
        return getOrThrow(path) {
            getBooleanOrNull(path)
        }
    }

    fun getBooleanOrNull(path: String): Boolean? = getBooleanOrDefault(path, null)

    fun getBooleanOrDefault(path: String, default: Boolean?): Boolean? {
        return getOrDefault(path, default) {
            handle.getBoolean(path)
        }
    }

    fun getInt(path: String): Int {
        return getOrThrow(path) {
            getIntOrNull(path)
        }
    }

    fun getIntOrNull(path: String): Int? = getIntOrDefault(path, null)

    fun getIntOrDefault(path: String, default: Int?): Int? {
        return getOrDefault(path, default) {
            handle.getInt(path)
        }
    }

    fun getDouble(path: String): Double {
        return getOrThrow(path) {
            getDoubleOrNull(path)
        }
    }

    fun getDoubleOrNull(path: String): Double? = getDoubleOrDefault(path, null)

    fun getDoubleOrDefault(path: String, default: Double?): Double? {
        return getOrDefault(path, default) {
            handle.getDouble(path)
        }
    }

    fun getString(path: String): String {
        return getOrThrow(path) {
            getStringOrNull(path)
        }
    }

    fun getStringOrNull(path: String): String? = getStringOrDefault(path, null)

    fun getStringOrDefault(path: String, default: String?): String? {
        return getOrDefault(path, default) {
            handle.getString(path)
        }
    }

    fun getShort(path: String): Short {
        return getOrThrow(path) {
            getShortOrNull(path)
        }
    }

    fun getShortOrNull(path: String): Short? = getShortOrDefault(path, 0)

    fun getShortOrDefault(path: String, default: Short?): Short? {
        return getOrDefault(path, default) {
            handle.getInt(path).toShort()
        }
    }

    fun <E : Enum<E>> getEnum(path: String, clazz: Class<E>): E = getOrThrow(path) {
        getEnumOrNull(path, clazz)
    }

    fun <E : Enum<E>> getEnumOrNull(path: String, clazz: Class<E>): E? = getEnumOrDefault(path, clazz, null)

    fun <E : Enum<E>> getEnumOrDefault(path: String, clazz: Class<E>, default: E?): E? {
        return Reflections.getEnum(clazz, getStringOrNull(path) ?: return default)
    }

    /**
     * 获取物品
     * 支持属性：material，sub-id, display-name, lores
     */
    fun getItemStack(path: String): ItemStack {
        return getOrThrow(path) {
            getItemStackOrNull(path)
        }
    }

    fun getItemStackOrNull(path: String): ItemStack? {
        return getItemStackOrDefault(path, null)
    }

    fun getItemStackOrDefault(path: String, default: ItemStack?): ItemStack? {
        return getOrDefault(path, default) {
            val itemSection = getSectionOrNull(path) ?: return@getOrDefault default

            ItemBuilder()
                .colored(itemSection.getBooleanOrDefault("colored", false)!!)
                .setMaterial(itemSection.getEnum("material", Material::class.java))
                .setSubId(itemSection.getShortOrDefault("sub-id", 0)!!)
                .setDisplayName(itemSection.getStringOrDefault("display-name", null))
                .setLores(itemSection.getStringList("lores").map { it.toColored() })
                .build()
        }
    }

    /**
     * 获取，如果为 null 则抛出异常
     * @param path 路径
     * @param supplier 生产者
     */
    private fun <T> getOrThrow(path: String, supplier: Supplier<T?>): T {
        return supplier.get() ?: throw IllegalArgumentException("Unable to get value by path: '$currentPath.$path'")
    }

    /**
     * 获取，如果为 null 则返回默认值
     * @param path 路径
     * @param default 默认值
     * @param supplier 生产者
     */
    private fun <T> getOrDefault(path: String, default: T?, supplier: Supplier<T?>): T? {
        try {
            return supplier.get() ?: default
        } catch (ex: Exception) {
            throw RuntimeException("An exception occurred while parsing $currentPath.$path", ex)
        }
    }
}