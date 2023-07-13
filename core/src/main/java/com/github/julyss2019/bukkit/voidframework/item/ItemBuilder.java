package com.github.julyss2019.bukkit.voidframework.item;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import com.github.julyss2019.bukkit.voidframework.common.Items;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemBuilder {
    private Material material;
    private int amount = 1;
    private short subId;
    private String displayName;
    private List<String> lores = new ArrayList<>();

    public ItemBuilder() {}

    /**
     * 设置材质
     */
    public ItemBuilder material(@NonNull Material material) {
        if (material == Material.AIR) {
            throw new IllegalArgumentException("cannot use Material.AIR");
        }

        this.material = material;
        return this;
    }

    /**
     * 设置材质
     * @param materialName 材质名
     */
    public ItemBuilder material(@NonNull String materialName) {
        this.material = Optional
                .ofNullable(Material.getMaterial(materialName))
                .orElseThrow(() -> new RuntimeException("invalid material string: " + materialName));
        return this;
    }

    /**
     * 设置材质
     * @param id 材质 ID
     */
    public ItemBuilder material(int id) {
        this.material = Items.getMaterialById(id);
        return this;
    }

    /**
     * 设置数量
     */
    public ItemBuilder amount(int amount) {
        Validator.checkState(amount > 0, "amount must > 0");

        this.amount = amount;
        return this;
    }

    public ItemBuilder subId(short subId) {
        this.subId = subId;
        return this;
    }

    /**
     * 设置子 ID
     */
    @Deprecated
    public ItemBuilder data(short data) {
        this.subId = data;
        return this;
    }/**/

    /**
     * 设置物品名
     */
    public ItemBuilder displayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    private void checkNullLoreInList(List<@NonNull String> lores) {
        Validator.checkNotContainsNullElement(lores, "lores cannot contains null element");
    }

    /**
     * 设置 lores
     */
    public ItemBuilder setLores(@NonNull List<@NonNull String> lores) {
        checkNullLoreInList(lores);

        this.lores = new ArrayList<>(lores);
        return this;
    }

    /**
     * 追加 lores
     */
    public ItemBuilder appendLores(@NonNull List<@NonNull String> lores) {
        checkNullLoreInList(lores);

        this.lores.addAll(lores);
        return this;
    }

    /**
     * 追加 lores
     */
    public ItemBuilder appendLores(@NonNull String... lores) {
        this.lores.addAll(Arrays.asList(lores));
        return this;
    }

    public ItemStack build() {
        if (material == null) {
            throw new RuntimeException("missing material");
        }

        ItemStack itemStack = new ItemStack(material, amount, subId);
        ItemMeta itemMeta = itemStack.getItemMeta();

        //noinspection ConstantConditions
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
