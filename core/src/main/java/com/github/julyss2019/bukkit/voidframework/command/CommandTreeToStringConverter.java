package com.github.julyss2019.bukkit.voidframework.command;


import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.OptionalUserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.UserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandElement;

import java.util.stream.Collectors;

public class CommandTreeToStringConverter {
    public static String convertToString(CommandTree commandTree) {
        StringBuilder messages = new StringBuilder();

        messages.append("&a/");

        String parentIds = commandTree.getParents(false)
                .stream()
                .map(commandTree1 -> commandTree1.getElement().getCommandId())
                .collect(Collectors.joining(" "));
        messages.append(parentIds);

        // 考虑 /tpa 这种没有父亲的情形
        if (parentIds.length() != 0) {
            messages.append(" ");
        }

        CommandElement element = commandTree.getElement();

        if (element instanceof CommandBodyElement) {
            messages.append(getSubCommandTreeString(commandTree, 0));
        } else {
            messages.append(element.getCommandId());
            messages.append("\n");

            for (CommandTree child : commandTree.getChildren()) {
                messages.append(getSubCommandTreeString(child, 4));
            }
        }

        return messages.toString();
    }

    private static String getSubCommandTreeString(CommandTree commandTree, int intent) {
        StringBuilder messages = new StringBuilder();

        for (int i = 0; i < intent; i++) {
            messages.append(" ");
        }

        CommandElement component = commandTree.getElement();

        messages.append("&a");
        messages.append(component.getCommandId());
        messages.append("&f");

        if (component instanceof CommandBodyElement) {
            CommandBodyElement commandBodyElement = (CommandBodyElement) component;

            messages.append(" ");

            for (UserInputMethodParam userInputMethodParam : commandBodyElement.getUserInputMethodParams()) {
                messages.append(formatParam(userInputMethodParam));
                messages.append(" ");
            }

            messages.append("&7");
            messages.append("-> ");
            messages.append("&b");
            messages.append(commandBodyElement.getDescription());
        } else {
            if (commandTree.hasChildren()) {
                messages.append("\n");
            }

            for (CommandTree commandTreeChild : commandTree.getChildren()) {
                messages.append(getSubCommandTreeString(commandTreeChild, intent + 2));
            }
        }

        messages.append("\n");
        return messages.toString();
    }

    private static String formatParam(UserInputMethodParam param) {
        String description = param.getDescription();

        if (param instanceof OptionalUserInputMethodParam) {
            return "[" + description + "]";
        } else {
            return "<" + description + ">";
        }
    }
}
