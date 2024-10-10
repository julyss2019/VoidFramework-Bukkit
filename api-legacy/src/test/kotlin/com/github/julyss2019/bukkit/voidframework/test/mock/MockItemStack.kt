package com.github.julyss2019.bukkit.voidframework.test.mock

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.mockito.Mockito.*

object MockItemStack {
    fun mock(material: Material, amount: Int): ItemStack {
        return mock(ItemStack(material, amount))
    }

    fun mock(itemStack: ItemStack): ItemStack {
        val spyItemStack = spy(itemStack)

        // 该方式打桩会报错, 因为此时 spyItemStack.itemMeta 是实际的对象.
        // `when`(spyItemStack.itemMeta).thenReturn(MockItemMeta())
        doReturn(MockItemMeta()).`when`(spyItemStack).itemMeta

        if (itemStack.type != Material.AIR) {
            doReturn(true).`when`(spyItemStack).hasItemMeta()
        }

        `when`(spyItemStack.isSimilar(any(ItemStack::class.java))).thenAnswer {
            val itemStack1: ItemStack = it.getArgument(0) ?: return@thenAnswer false

            itemStack.type == itemStack1.type
                    && spyItemStack.itemMeta == itemStack1.itemMeta
        }
        `when`(spyItemStack.clone()).thenAnswer {
            mock(itemStack)
        }
        doAnswer {
            "ItemStack(type=${spyItemStack.type}, itemMeta=${spyItemStack.itemMeta})"
        }.`when`(spyItemStack).toString()
        return spyItemStack
    }
}