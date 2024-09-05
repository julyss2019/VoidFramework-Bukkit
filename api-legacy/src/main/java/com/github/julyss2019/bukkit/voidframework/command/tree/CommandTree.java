package com.github.julyss2019.bukkit.voidframework.command.tree;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandElement;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 命令 N 叉树
 */
public class CommandTree {
    private CommandTree parent;
    private final CommandElement element;
    private final List<CommandTree> children = new ArrayList<>();

    public CommandTree(@Nullable CommandElement element) {
        this.element = element;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(CommandTree parent) {
        this.parent = parent;
    }

    public void removeChild(@NonNull CommandTree tree) {
        children.remove(tree);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public CommandElement getCommandElement() {
        return element;
    }

    public CommandTree getOrAddChild(@NonNull CommandTree tree) {
        CommandTree child = children.stream()
                .filter(commandTree -> commandTree.getCommandElement().getCommandId().equalsIgnoreCase(tree.element.getCommandId()))
                .findAny()
                .orElse(null);

        if (child == null) {
            return addChild(tree);
        }

        return child;
    }

    public CommandTree addChild(@NonNull CommandTree tree) {
        if (tree.getCommandElement() == null) {
            throw new RuntimeException("tree component cannot be null");
        }

        for (CommandTree child : children) {
            if (tree.getCommandElement().getCommandId().equalsIgnoreCase(child.getCommandElement().getCommandId())) {
                throw new RuntimeException(String.format("same id child already exists: %s", child.getCommandElement().getCommandId()));
            }
        }

        tree.setParent(this);
        children.add(tree);
        return tree;
    }

    public CommandTree getParent() {
        return parent;
    }

    public List<CommandTree> getChildren() {
        return children;
    }

    public String getTreeAsString() {
        return getTreeAsString(0);
    }

    private String getTreeAsString(int level) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < level * 2; i++) {
            stringBuilder.append(" ");
        }

        if (element != null) {
            stringBuilder
                    .append(element.getCommandId())
                    .append("(")
                    .append(element.getActiveCommandGroup().getPlugin().getName());

            if (element instanceof CommandBodyElement) {
                CommandBodyElement commandBodyElement = (CommandBodyElement) element;

                stringBuilder
                        .append(", ")
                        .append(commandBodyElement.getMinInputParamCount())
                        .append(", ").append(commandBodyElement.getMaxInputParamCount());
            }


            stringBuilder.append(")");
            stringBuilder.append("\n");
        } else {
            stringBuilder.append("root\n");
        }

        for (CommandTree child : getChildren()) {
            stringBuilder.append(child.getTreeAsString(level + 1));
        }

        return stringBuilder.toString();
    }

    public String getTreeAsCommandLine() {
        List<String> commandIds = new ArrayList<>();
        CommandTree currentTree = this;

        while (currentTree != null) {
            CommandElement element = currentTree.element;

            if (element != null) {
                commandIds.add(element.getCommandId());
            }

            currentTree = currentTree.getParent();
        }

        Collections.reverse(commandIds);
        return "/" + String.join(" ", commandIds);
    }

    public int getLevel() {
        int tmp = 0;
        CommandTree parent = getParent();

        while (parent != null) {
            parent = parent.getParent();
            tmp++;
        }

        return tmp;
    }

    public List<CommandTree> getParents(boolean withRoot) {
        CommandTree currentTree = this;
        List<CommandTree> parents = new ArrayList<>();

        while (currentTree.hasParent()) {
            currentTree = currentTree.getParent();

            if (!withRoot && currentTree instanceof RootCommandTree) {
                break;
            }

            parents.add(0, currentTree);
        }

        return parents;
    }

    @Override
    public String toString() {
        return getTreeAsString();
    }
}
