package com.github.julyss2019.bukkit.voidframework.command.failure;

import com.github.julyss2019.bukkit.voidframework.command.CommandLineArrayToStringConverter;
import com.github.julyss2019.bukkit.voidframework.command.CommandTreeToStringConverter;
import com.github.julyss2019.bukkit.voidframework.command.SenderType;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.common.Messages;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class CommandFailureHandlerImpl implements CommandFailureHandler {
    private final LocaleResource failureLocaleResource;

    public CommandFailureHandlerImpl(@NonNull LocaleResource localeResource) {
        this.failureLocaleResource = localeResource.getLocalResource("command.failure-handler");
    }

    @Override
    public void onMissingPermission(CommandSender sender, CommandTree commandTree, String[] commandLineArray, String[] permissions) {
        Messages.sendColoredPlaceholderMessage(sender, failureLocaleResource.getString("missing-permission"),
                new PlaceholderContainer()
                        .put("command-line", CommandLineArrayToStringConverter.convertToString(commandLineArray))
                        .put("permissions", String.join(failureLocaleResource.getString("permission-or"), commandLineArray)));
    }

    /**
     * 命令格式错误回调
     *
     */
    public void onCommandFormatError(CommandSender sender,
                                     CommandTree commandTree,
                                     String[] commandLineArray) {
        Messages.sendColoredPlaceholderMessage(sender, failureLocaleResource.getString("format-error"),
                new PlaceholderContainer()
                        .put("command-line", CommandLineArrayToStringConverter.convertToString(commandLineArray))
                        .put("available-command-trees", CommandTreeToStringConverter.convertToString(commandTree)));
    }

    public void onCommandParamParseError(CommandSender sender,
                                         CommandTree commandTree,
                                         String[] commandLineArray,
                                         int paramIndex,
                                         String errorMessage) {
        StringBuilder commandLine = new StringBuilder();

        for (int i = 0; i < commandLineArray.length; i++) {
            if (i == paramIndex) {
                commandLine.append("&e");
                commandLine.append(commandLineArray[i]);
            } else {
                commandLine.append("&a");
                commandLine.append(commandLineArray[i]);
                commandLine.append("&c");
            }

            if (i != commandLineArray.length - 1) {
                commandLine.append(" ");
            }
        }

        Messages.sendColoredPlaceholderMessage(sender, failureLocaleResource.getString("param-parse-error"),
                new PlaceholderContainer()
                        .put("command-line", commandLine.toString())
                        .put("param", commandLineArray[paramIndex])
                        .put("error-message", errorMessage));
    }

    @Override
    public void onCommandSenderMismatch(CommandSender sender, CommandTree commandTree, String[] commandLineArray) {
        CommandBodyElement element = (CommandBodyElement) commandTree.getElement();
        String senderLocaleStr;

        if (element.getSenderTypes()[0] == SenderType.PLAYER) {
            senderLocaleStr = failureLocaleResource.getString("sender-player");
        } else {
            senderLocaleStr = failureLocaleResource.getString("sender-console");
        }

        Messages.sendColoredPlaceholderMessage(sender, failureLocaleResource.getString("sender-mismatch"),
                new PlaceholderContainer()
                        .put("command-line", CommandLineArrayToStringConverter.convertToString(commandLineArray))
                        .put("sender", senderLocaleStr));
    }
}
