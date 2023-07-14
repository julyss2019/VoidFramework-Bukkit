package com.github.julyss2019.bukkit.voidframework.common.internal;

import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemSimilarPredicate implements Predicate<ItemStack> {
    private final ItemStack item;

    public ItemSimilarPredicate(@NonNull ItemStack item) {
        this.item = item.clone();
    }

    @Override
    public boolean test(ItemStack targetItem) {
        return this.item.isSimilar(targetItem);
    }
}
