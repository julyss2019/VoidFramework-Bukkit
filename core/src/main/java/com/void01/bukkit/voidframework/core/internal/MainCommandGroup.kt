package com.void01.bukkit.voidframework.core.internal

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import com.void01.bukkit.voidframework.core.component.ComponentManagerImpl
import com.void01.bukkit.voidframework.core.internal.util.MessageUtils
import com.void01.bukkit.voidframework.core.script.ScriptManagerImpl
import org.bukkit.command.CommandSender

class MainCommandGroup(private val plugin: VoidFrameworkPlugin) : CommandGroup {
    @CommandBody(value = "reload", description = "重载")
    fun reload(sender: CommandSender) {
        plugin.reload()
        MessageUtils.sendMessage(sender, "重载完毕.")
    }

    @CommandBody(value = "unloadComponents", description = "卸载所有组件")
    fun unloadComponents(sender: CommandSender) {
        (plugin.componentManager as ComponentManagerImpl).unloadComponents()
        MessageUtils.sendMessage(sender, "已卸载所有组件.")
    }
}