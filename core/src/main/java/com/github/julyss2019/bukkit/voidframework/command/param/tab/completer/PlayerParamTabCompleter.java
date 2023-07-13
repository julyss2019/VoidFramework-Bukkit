package com.github.julyss2019.bukkit.voidframework.command.param.tab.completer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerParamTabCompleter extends BaseParamTabCompleter {
    public PlayerParamTabCompleter() {
        super(new Class[]{Player.class});
    }

    @Override
    public List<String> complete(CommandSender sender, Class<?> paramType) {
        return Bukkit
                .getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
