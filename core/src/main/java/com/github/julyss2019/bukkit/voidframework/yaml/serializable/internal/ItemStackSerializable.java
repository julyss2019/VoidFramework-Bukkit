package com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal;

import com.github.julyss2019.bukkit.voidframework.common.Strings;
import com.github.julyss2019.bukkit.voidframework.item.ItemBuilder;
import com.github.julyss2019.bukkit.voidframework.common.Items;
import com.github.julyss2019.bukkit.voidframework.text.Texts;
import lombok.NonNull;
import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue;
import com.github.julyss2019.bukkit.voidframework.yaml.Paths;
import com.github.julyss2019.bukkit.voidframework.yaml.Section;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStackSerializable implements Serializable<ItemStack> {
    private static ItemStackSerializable instance;

    public static ItemStackSerializable getInstance() {
        if (instance == null) {
            instance = new ItemStackSerializable();
        }

        return instance;
    }

    @Override
    public void set(@NonNull Section parent, @NonNull String path, @Nullable ItemStack itemStack) {
        if (itemStack == null) {
            parent.setByBukkit(path, null);
            return;
        }

        Section itemSection = parent.getOrCreateSection(path);

        itemSection.setByBukkit("amount", itemStack.getAmount());
        itemSection.setByBukkit("material", itemStack.getType());
        itemSection.setByBukkit("sub-id", itemStack.getDurability());
        itemSection.setByBukkit("display-name", Items.getDisplayName(itemStack));
        itemSection.setByBukkit("lores", Items.getLores(itemStack));
    }

    @Override
    public ItemStack get(@NonNull Section parent, @NonNull String path) {
        Section itemSection = parent.getSection(Paths.of(path), null);
        ItemBuilder itemBuilder = new ItemBuilder();

        boolean colored = itemSection.getBoolean("colored", DefaultValue.of(true));

        itemBuilder
                .amount(itemSection.getInt(Paths.of("amount"), DefaultValue.of(1)))
                .data(itemSection.getShort(Paths.of("data", "sub_id", "sub-id"), DefaultValue.of((short) 0)));

        String displayName = itemSection.getString(Paths.of("display_name", "display-name"), DefaultValue.of(null));
        List<String> lores = itemSection.getStringList("lores");

        if (colored) {
            if (displayName != null) {
                displayName = Texts.getColoredText(displayName);
            }

            lores = Texts.getColoredTexts(lores);
        }

        itemBuilder
                .displayName(displayName)
                .setLores(lores);

        String materialStr = itemSection.getString(Paths.of("id", "material", "mat"), null);

        if (Strings.isInteger(materialStr)) {
            itemBuilder.material(Integer.parseInt(materialStr));
        } else {
            itemBuilder.material(materialStr);
        }

        return itemBuilder.build();
    }
}
