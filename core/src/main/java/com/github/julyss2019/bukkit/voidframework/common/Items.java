package com.github.julyss2019.bukkit.voidframework.common;


import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.annotation.ValidItem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Items {
    public static ItemStack setSkullTexture(@ValidItem ItemStack itemStack, @NonNull String texture) {
        checkValid(itemStack);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!(itemMeta instanceof SkullMeta)) {
            throw new IllegalArgumentException("itemStack is not Skull");
        }

        SkullMeta headMeta = (SkullMeta) itemMeta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", texture));
        Reflections.setDeclaredFieldValue(headMeta.getClass(), "profile", headMeta, profile);
        itemStack.setItemMeta(headMeta);
        return itemStack;
    }

    public static @NonNull List<String> getLores(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) {
            return new ArrayList<>();
        }

        return Optional
                .ofNullable(itemStack.getItemMeta().getLore())
                .orElse(new ArrayList<>());
    }

    public static @NonNull List<String> setLores(@ValidItem ItemStack itemStack, @NonNull List<String> lores) {
        checkValid(itemStack);

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return lores;
    }

    public static String getDisplayName(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) {
            return null;
        }

        return itemStack.getItemMeta().getDisplayName();
    }

    public static void checkValid(ItemStack itemStack) {
        Validator.checkState(isValid(itemStack), "itemStack is invalid");
    }

    public static boolean isValid(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

    public static Material getMaterialById(int id) {
        if (id == 0) {
            return Material.AIR;
        }

        return (Material) Reflections.invokeMethod(Reflections.newInstance(Material.class),
                "getMaterial",
                id);
    }

    public static int getTypeId(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }

        return (int) Reflections.getDeclaredFieldValue(ItemStack.class, "getTypeId", itemStack);
    }
}
