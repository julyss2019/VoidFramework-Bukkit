package com.github.julyss2019.bukkit.voidframework.internal.command;

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup;
import com.github.julyss2019.bukkit.voidframework.command.SenderType;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandParam;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandMapping(value = "demo")
public class DemoCommandGroup implements CommandGroup {
    @CommandBody(value = "giveItem", description = "给予玩家物品")
    public void giveItem(CommandSender sender,
                         @CommandParam(description = "物品") Material material,
                         @CommandParam(description = "数量") int amount,
                         @CommandParam(description = "玩家") Player player) {
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(new ItemStack(material, 1));
        }

        sender.sendMessage("OK.");
    }

    @CommandBody(value = "kill", description = "杀人或自杀")
    public void kill(CommandSender sender, @CommandParam(description = "玩家(不填为自杀)", optional = true) Player player) {
        if (player == null) {
            if (sender instanceof Player) {
                ((Player) sender).setHealth(0);
            } else {
                sender.sendMessage("自杀只允许玩家来执行");
                return;
            }
        } else {
            player.setHealth(0);
        }

        sender.sendMessage("OK.");
    }
}
