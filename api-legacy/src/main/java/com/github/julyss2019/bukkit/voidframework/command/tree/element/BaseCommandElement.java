package com.github.julyss2019.bukkit.voidframework.command.tree.element;

import com.github.julyss2019.bukkit.voidframework.command.CommandGroupContext;
import lombok.NonNull;

public abstract class BaseCommandElement implements CommandElement {
    private CommandGroupContext holder;
    private String commandId;
    private String permission;

    public BaseCommandElement() {
    }

    public BaseCommandElement(@NonNull CommandGroupContext holder, @NonNull String commandId, @NonNull String permission) {
        this.holder = holder;
        this.commandId = commandId;
        this.permission = permission;
    }

    public void setHolder(@NonNull CommandGroupContext holder) {
        this.holder = holder;
    }

    public void setCommandId(@NonNull String commandId) {
        this.commandId = commandId;
    }

    public void setPermission(@NonNull String permission) {
        this.permission = permission;
    }

    @Override
    public String getCommandId() {
        return commandId;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public CommandGroupContext getActiveCommandGroup() {
        return holder;
    }
}
