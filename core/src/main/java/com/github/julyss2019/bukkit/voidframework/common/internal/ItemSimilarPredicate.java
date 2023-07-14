package com.github.julyss2019.bukkit.voidframework.common.internal;

import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemSimilarPredicate implements Predicate<ItemStack> {
    private final ItemStack itemStack;

    public ItemSimilarPredicate(@NonNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    @Override
    public boolean test(ItemStack item) {
        return itemStack.isSimilar(itemStack);
    }
}
