package com.github.julyss2019.bukkit.voidframework.common;

import com.github.julyss2019.bukkit.voidframework.annotation.ValidItem;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;

public class Players {
    /**
     * 给予物品，若背包已满则掉落在玩家所在的位置
     */
    @Deprecated
    public static void giveItemOrDrop(@NonNull Player player, ItemStack itemStack) {
        Validator.checkState(Items.isValid(itemStack), "itemStack is invalid");

        PlayerInventory inventory = player.getInventory();

        if (inventory.firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            inventory.addItem(itemStack);
        }
    }

    /**
     * 往背包放置物品
     * 槽位仅包含：从下往上数四行
     * 以 item 的数量作为预期拿走物品的数量
     *
     * @param player 玩家
     * @param item   物品
     * @return 成功放置的数量
     */
    public static int putItems(@NonNull Player player, @ValidItem ItemStack item) {
        return putItems(player, item, item.getAmount());
    }

    /**
     * 往背包放置物品
     * 槽位仅包含：从下往上数四行
     *
     * @param player 玩家
     * @param item   物品
     * @param amount 预期拿走物品的数量
     * @return 成功放置的数量
     */
    public static int putItems(@NonNull Player player, @ValidItem ItemStack item, int amount) {
        return Inventories.putItems(player.getInventory(), Inventories.getPlayerInventoryBottom4RowSlots(), item, amount);
    }

    /**
     * 拿走背包的物品
     * 槽位仅包含：从下往上数四行
     * 以 item 的数量作为预期拿走物品的数量
     *
     * @param player 玩家
     * @param item   物品
     * @return 成功拿走的数量
     */
    public static int takeInventoryItems(@NonNull Player player, @ValidItem ItemStack item) {
        return takeInventoryItems(player, item, item.getAmount());
    }

    /**
     * 拿走背包的物品
     * 槽位仅包含：从下往上数四行
     *
     * @param player 玩家
     * @param item   物品
     * @param amount 数量
     * @return 成功拿走的数量
     */
    public static int takeInventoryItems(@NonNull Player player, @ValidItem ItemStack item, int amount) {
        return Inventories.takeItems(player.getInventory(), Inventories.getPlayerInventoryBottom4RowSlots(), item, amount);
    }

    /**
     * 计算背包物品数量
     * 槽位仅包含：从下往上数四行
     *
     * @param player    玩家
     * @param itemStack 物品
     * @return 背包物品数量
     */
    public static int calculateInventoryItemCount(@NonNull Player player, @ValidItem ItemStack itemStack) {
        return calculateInventoryItemCount(player, Inventories.getPlayerInventoryBottom4RowSlots(), itemStack);
    }

    /**
     * 计算玩家背包物品数量
     *
     * @param player    玩家
     * @param slots     槽位 id
     * @param itemStack 物品
     * @return 背包物品数量
     */
    public static int calculateInventoryItemCount(@NonNull Player player, @NonNull List<Integer> slots, @ValidItem ItemStack itemStack) {
        return Inventories.calculateItemCount(player.getInventory(), slots, itemStack);
    }

    /**
     * 检查玩家在线状态
     *
     * @param uuid uuid
     * @return 是否在线
     */
    public static boolean isOnline(@NonNull UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    /**
     * 检查玩家在线状态
     *
     * @param name 玩家名
     * @return 是否在线
     */
    public static boolean isOnline(@NonNull String name) {
        Player player = Bukkit.getPlayer(name);

        return player != null && player.getName().equals(name);
    }
}
