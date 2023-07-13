package com.github.julyss2019.bukkit.voidframework.internal.command;

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import org.bukkit.command.CommandSender;

@CommandMapping(value = "demo/a")
public class DemoACommandGroup  implements CommandGroup {
    @CommandBody(value = "test", description = "test")
    public void test(CommandSender sender) {
        sender.sendMessage("test");
    }
}
