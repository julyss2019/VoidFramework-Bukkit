package com.void01.bukkit.voidframework.commons.util

import com.void01.bukkit.voidframework.commons.extension.isValid
import org.bukkit.inventory.ItemStack

fun checkValidItemStack(
    itemStack: ItemStack?,
    lazyMessage: () -> Any = {
        "Invalid ItemStack"
    }
) {
    check(itemStack.isValid, lazyMessage)
}