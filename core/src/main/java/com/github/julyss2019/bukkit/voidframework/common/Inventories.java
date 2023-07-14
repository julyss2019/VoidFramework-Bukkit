package com.github.julyss2019.bukkit.voidframework.common;

import com.github.julyss2019.bukkit.voidframework.annotation.ValidItem;
import com.github.julyss2019.bukkit.voidframework.common.internal.ItemSimilarPredicate;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Inventories {
    private static final ItemStack AIR_ITEM = new ItemStack(Material.AIR);
    private static final List<Integer> PLAYER_INVENTORY_BOTTOM_4_ROW_SLOTS = new ArrayList<>();
    private static final List<Integer> PLAYER_INVENTORY_ALL_SLOTS = new ArrayList<>();

    static {
        for (int i = 0; i < 36; i++) {
            PLAYER_INVENTORY_BOTTOM_4_ROW_SLOTS.add(i);
        }

        for (int i = 0; i < 45; i++) {
            PLAYER_INVENTORY_ALL_SLOTS.add(i);
        }
    }

    /**
     * 获取玩家背包的主槽位
     * 包括：从底部往上数 4 行，36 个槽
     */
    @Deprecated
    public static List<Integer> getPlayerInventoryMainSlots() {
        return Collections.unmodifiableList(PLAYER_INVENTORY_BOTTOM_4_ROW_SLOTS);
    }

    /**
     * 获取玩家背包的主槽位
     * 包括：从底部往上数 4 行，36 个槽
     */
    public static List<Integer> getPlayerInventoryBottom4RowSlots() {
        return Collections.unmodifiableList(PLAYER_INVENTORY_BOTTOM_4_ROW_SLOTS);
    }

    /**
     * 获取玩家背包的所有槽位
     */
    public static List<Integer> getPlayerInventoryAllSlots() {
        return Collections.unmodifiableList(PLAYER_INVENTORY_ALL_SLOTS);
    }

    /**
     * 拿走背包的物品(以 item 的数量作为数量)
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     * @return 拿走的数量
     */
    public static int takeItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @ValidItem ItemStack item) {
        Validator.checkValidItem(item);

        return takeItems(inventory, slots, new ItemSimilarPredicate(item), item.getAmount());
    }

    /**
     * 拿走背包的物品
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     * @param amount    数量
     * @return 拿走的数量
     */
    public static int takeItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @ValidItem ItemStack item, int amount) {
        Validator.checkValidItem(item);

        return takeItems(inventory, slots, new ItemSimilarPredicate(item), amount);
    }

    /**
     * 拿走背包的物品
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param predicate 依据
     * @param amount    数量
     * @return 拿走的数量
     */
    public static int takeItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @NonNull Predicate<ItemStack> predicate, int amount) {
        Validator.checkState(amount > 0, "amount must > 0");
        Validator.checkNotContainsNullElement(slots, "slots cannot contains null");

        int finishedAmount = 0;

        for (int slot : slots) {
            ItemStack item = inventory.getItem(slot);

            if (!Items.isValid(item)) {
                continue;
            }

            if (predicate.test(item)) {
                int itemAmount = item.getAmount();

                if (finishedAmount + itemAmount > amount) {
                    ItemStack newAmountItem = item.clone();

                    newAmountItem.setAmount(amount - finishedAmount);
                    inventory.setItem(slot, newAmountItem);
                    finishedAmount = amount;
                } else {
                    inventory.setItem(slot, null);
                    finishedAmount += itemAmount;
                }
            }

            if (finishedAmount == amount) {
                return finishedAmount;
            }
        }

        return finishedAmount;
    }

    /**
     * 往背包放置物品(数量为提供的 item 的数量)
     * 若可堆叠则会进行堆叠
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     */
    public static int putItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @ValidItem ItemStack item) {
        Validator.checkValidItem(item);

        return putItems(inventory, slots, item, item.getAmount());
    }

    /**
     * 往背包放置物品
     * 若可堆叠则会进行堆叠
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     * @param amount    数量
     */
    public static int putItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @ValidItem ItemStack item, int amount) {
        Validator.checkValidItem(item);
        Validator.checkNotContainsNullElement(slots, "slots cannot contains null");

        int maxStack = item.getMaxStackSize();
        int finishedAmount = 0; // 已经放置的

        // 能叠的先叠
        for (int slot : slots) {
            ItemStack invItem = inventory.getItem(slot);

            if (invItem != null && invItem.isSimilar(item)) {
                int oldAmount = invItem.getAmount(); // 旧物品数量
                int leftAmount = maxStack - oldAmount; // 旧物品剩余可用的数量
                int addAmount = Math.min(amount - finishedAmount, leftAmount); // 新物品增加的数量
                int newAmount = leftAmount + addAmount; // 新物品数量

                ItemStack newItem = item.clone();

                newItem.setAmount(newAmount);
                inventory.setItem(slot, newItem);
                finishedAmount += addAmount;
            }

            if (finishedAmount == amount) {
                return amount;
            }
        }

        // 利用空格子
        for (int slot : slots) {
            ItemStack invItem = inventory.getItem(slot);

            if (!Items.isValid(invItem)) {
                ItemStack newItem = item.clone();
                int newAmount = Math.min(amount - finishedAmount, maxStack);

                newItem.setAmount(newAmount);
                inventory.setItem(slot, newItem);
                finishedAmount += newAmount;
            }

            if (finishedAmount == amount) {
                return amount;
            }
        }

        return finishedAmount;
    }

    /**
     * 判断背包是否能放下物品(数量为提供的 item 的数量)
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     * @return 是否能放下
     */
    public static boolean canPutItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @NonNull ItemStack item) {
        return canPutItems(inventory, slots, item, item.getAmount());
    }

    /**
     * 判断背包是否能放下物品
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param item      物品
     * @param amount    数量
     * @return 是否能放下
     */
    public static boolean canPutItems(@NonNull Inventory inventory, @NonNull List<Integer> slots, @NonNull ItemStack item, int amount) {
        int itemMaxStack = item.getMaxStackSize();
        int availableAmount = 0;

        for (Integer slot : slots) {
            ItemStack invItem = inventory.getItem(slot);

            if (!Items.isValid(invItem)) {
                availableAmount += itemMaxStack;
            } else if (invItem.isSimilar(item)) {
                availableAmount += (itemMaxStack - invItem.getAmount());
            }

            if (availableAmount >= amount) {
                return true;
            }
        }

        return availableAmount >= amount;
    }

    /**
     * 计算背包物品数量
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param itemStack 物品
     * @return 总数
     */
    public static int calculateItemCount(@NonNull Inventory inventory, @NonNull List<Integer> slots, @NonNull ItemStack itemStack) {
        return calculateItemCount(inventory, slots, new ItemSimilarPredicate(itemStack));
    }

    /**
     * 计算背包物品数量
     *
     * @param inventory 背包
     * @param slots     槽位 id
     * @param predicate 依据
     * @return 总数
     */
    public static int calculateItemCount(@NonNull Inventory inventory, @NonNull List<Integer> slots, @NonNull Predicate<ItemStack> predicate) {
        Validator.checkNotContainsNullElement(slots, "slots cannot contains null");

        int sum = 0;

        for (Integer slot : slots) {
            ItemStack item = inventory.getItem(slot);

            if (item == null) {
                item = AIR_ITEM;
            }

            if (predicate.test(item)) {
                sum += item.getAmount();
            }
        }

        return sum;
    }
}
