package com.github.julyss2019.bukkit.voidframework.test.mock

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.mockito.Mockito.spy

open class MockInventory : Inventory {
    companion object {
        fun mock(): MockInventory {
            return spy(MockInventory())
        }
    }

    private val itemMap = mutableMapOf<Int, ItemStack?>()

    override fun iterator(): MutableListIterator<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun iterator(index: Int): MutableListIterator<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun getSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxStackSize(): Int {
        TODO("Not yet implemented")
    }

    override fun setMaxStackSize(size: Int) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getItem(index: Int): ItemStack? {
        return itemMap[index]
    }

    override fun setItem(index: Int, item: ItemStack?) {
        itemMap[index] = item
    }

    override fun addItem(vararg items: ItemStack?): HashMap<Int, ItemStack> {
        TODO("Not yet implemented")
    }

    override fun removeItem(vararg items: ItemStack?): HashMap<Int, ItemStack> {
        TODO("Not yet implemented")
    }

    override fun getContents(): Array<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun setContents(items: Array<out ItemStack>?) {
        TODO("Not yet implemented")
    }

    override fun getStorageContents(): Array<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun setStorageContents(items: Array<out ItemStack>?) {
        TODO("Not yet implemented")
    }

    override fun contains(materialId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(material: Material?): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(item: ItemStack?): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(materialId: Int, amount: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(material: Material?, amount: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(item: ItemStack?, amount: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAtLeast(item: ItemStack?, amount: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun all(materialId: Int): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun all(material: Material?): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun all(item: ItemStack?): HashMap<Int, out ItemStack> {
        TODO("Not yet implemented")
    }

    override fun first(materialId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun first(material: Material?): Int {
        TODO("Not yet implemented")
    }

    override fun first(item: ItemStack?): Int {
        TODO("Not yet implemented")
    }

    override fun firstEmpty(): Int {
        TODO("Not yet implemented")
    }

    override fun remove(materialId: Int) {
        TODO("Not yet implemented")
    }

    override fun remove(material: Material?) {
        TODO("Not yet implemented")
    }

    override fun remove(item: ItemStack?) {
        TODO("Not yet implemented")
    }

    override fun clear(index: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun getViewers(): MutableList<HumanEntity> {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun getType(): InventoryType {
        TODO("Not yet implemented")
    }

    override fun getHolder(): InventoryHolder {
        TODO("Not yet implemented")
    }

    override fun getLocation(): Location {
        TODO("Not yet implemented")
    }
}