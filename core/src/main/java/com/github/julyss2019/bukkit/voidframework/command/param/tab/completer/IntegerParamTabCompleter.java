package com.github.julyss2019.bukkit.voidframework.command.param.tab.completer;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class IntegerParamTabCompleter extends BaseParamTabCompleter {
    private static final List<String> COMPLETE_CONTENTS = new ArrayList<>();

    static {
        for (int i = 1; i < 11; i++) {
            COMPLETE_CONTENTS.add(String.valueOf(((int) Math.pow(2, i))));
        }
    }

    public IntegerParamTabCompleter() {
        super(new Class[]{int.class, Integer.class});
    }

    @Override
    public List<String> complete(CommandSender sender, Class<?> paramType) {
        return COMPLETE_CONTENTS;
    }
}
