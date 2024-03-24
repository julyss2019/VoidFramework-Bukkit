package com.void01.bukkit.voidframework.core.internal

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import com.void01.bukkit.voidframework.core.internal.util.MessageUtils
import org.bukkit.command.CommandSender

class MainCommandGroup(private val plugin: VoidFrameworkPlugin) : CommandGroup {
    @CommandBody(value = "reload", description = "重载")
    fun reload(sender: CommandSender) {
        MessageUtils.sendMessage(sender, "重载完毕.")
    }
}