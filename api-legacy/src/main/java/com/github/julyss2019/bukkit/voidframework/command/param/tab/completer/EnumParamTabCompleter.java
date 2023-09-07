package com.github.julyss2019.bukkit.voidframework.command.param.tab.completer;

import com.github.julyss2019.bukkit.voidframework.common.Reflections;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumParamTabCompleter extends BaseParamTabCompleter {
    public EnumParamTabCompleter() {
        super(new Class[]{Enum.class});
    }

    @Override
    public List<String> complete(CommandSender sender, Class<?> paramType) {
        return Arrays
                .stream(Reflections.getEnums(paramType))
                .map(o -> ((Enum<?>) o).name())
                .sorted()
                .collect(Collectors.toList());
    }
}
