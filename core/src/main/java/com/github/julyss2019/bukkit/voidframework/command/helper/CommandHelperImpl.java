package com.github.julyss2019.bukkit.voidframework.command.helper;


import com.github.julyss2019.bukkit.voidframework.command.CommandLineArrayToStringConverter;
import com.github.julyss2019.bukkit.voidframework.command.CommandTreeToStringConverter;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.common.Messages;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class CommandHelperImpl implements CommandHelper {
    private final LocaleResource helperLocaleResource;

    public CommandHelperImpl(@NonNull LocaleResource localeResource) {
        this.helperLocaleResource = localeResource.getLocalResource("command.helper");
    }

    @Override
    public void onHelp(CommandSender sender, CommandTree commandTree, String[] commandLineArray) {
        Messages.sendColoredPlaceholderMessage(sender, helperLocaleResource.getString("help"), new PlaceholderContainer()
                .put("command-line", CommandLineArrayToStringConverter.convertToString(commandLineArray))
                .put("available-command-trees", CommandTreeToStringConverter.convertToString(commandTree)));
    }
}
