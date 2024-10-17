package com.void01.bukkit.voidframework.api.common.logger.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.inventory.ItemStack

/**
 * [ItemStackTypeAdapter] 解决了 GSON 序列化 [ItemStack] 时出现 [StackOverflowError] 的问题.
 */
class ItemStackTypeAdapter : TypeAdapter<ItemStack>() {
    override fun write(writer: JsonWriter, obj: ItemStack?) {
        if (obj == null) {
            writer.nullValue()
        } else {
            writer.value(obj.toString())
        }
    }

    override fun read(reader: JsonReader): ItemStack? {
        return null
    }
}