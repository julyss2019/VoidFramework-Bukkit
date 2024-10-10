package com.github.julyss2019.bukkit.voidframework.test

import com.github.julyss2019.bukkit.voidframework.common.Inventories
import com.github.julyss2019.bukkit.voidframework.test.mock.MockInventory
import com.github.julyss2019.bukkit.voidframework.test.mock.MockItemStack
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InventoriesTest {
    private lateinit var inventory: Inventory
    private lateinit var airItem: ItemStack
    private lateinit var diamondItem: ItemStack
    private lateinit var testSlots: List<Int>

    @BeforeEach
    fun setup() {
        this.inventory = MockInventory.mock()
        this.airItem = MockItemStack.mock(Material.AIR, 1)
        this.diamondItem = MockItemStack.mock(Material.DIAMOND, 1)
        this.testSlots = listOf(0, 1, 2, 3)

        inventory.setItem(0, MockItemStack.mock(Material.DIAMOND, 5))
        inventory.setItem(1, MockItemStack.mock(Material.DIAMOND, 3))
        inventory.setItem(2, airItem)
        inventory.setItem(3, MockItemStack.mock(Material.STONE, 1))
    }

    @Test
    fun testGetItemAmount() {
        assertEquals(8, Inventories.getItemAmount(inventory, testSlots, diamondItem))
        assertEquals(0, Inventories.getItemAmount(inventory, testSlots) { it.type == Material.DIRT })
    }

    @Test
    fun testCanPutItemsCompletelyCompletely() {
        assertTrue(Inventories.canPutItemsCompletely(inventory, testSlots, diamondItem))
        assertFalse(Inventories.canPutItemsCompletely(inventory, testSlots, diamondItem, 59 + 61 + 64 + 1))
    }

    @Test
    fun testPutItems() {
        Inventories.putItems(inventory, testSlots, diamondItem, 59)
        Inventories.putItems(inventory, testSlots, diamondItem, 61)
        Inventories.putItems(inventory, testSlots, diamondItem)
        assertEquals(64, inventory.getItem(0).amount)
        assertEquals(64, inventory.getItem(1).amount)
        assertEquals(1, inventory.getItem(2).amount)
    }

    @Test
    fun testTakeItems() {
        assertEquals(1, Inventories.takeItems(inventory, testSlots, diamondItem))
        assertEquals(4, inventory.getItem(0).amount)

        assertEquals(7, Inventories.takeItems(inventory, testSlots, diamondItem, 10))
        assertEquals(null, inventory.getItem(0))
        assertEquals(null, inventory.getItem(1))
    }

    @Test
    fun testExtras() {
        Inventories.getPlayerInventoryAllSlots()
        Inventories.getPlayerInventoryBottom4RowSlots()
        Inventories.getPlayerInventoryMainSlots()
    }
}
