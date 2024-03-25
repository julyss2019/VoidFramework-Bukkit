package com.void01.bukkit.voidframework.core.internal

import com.github.julyss2019.bukkit.voidframework.command.CommandGroup
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import com.void01.bukkit.voidframework.core.internal.util.MessageUtils
import com.void01.bukkit.voidframework.core.script.ScriptManagerImpl
import org.bukkit.command.CommandSender

class MainCommandGroup(private val plugin: VoidFrameworkPlugin) : CommandGroup {
    @CommandBody(value = "reload", description = "重载")
    fun reload(sender: CommandSender) {
        plugin.reload()
        MessageUtils.sendMessage(sender, "重载完毕.")
    }

    // @CommandBody(value = "teseGC", description = "测试 GC")
    fun testScriptGC(sender: CommandSender) {
        for (i in 0 until 1000) {
            ScriptManagerImpl(plugin)
        }

        sender.sendMessage("Done")
    }

    // @CommandBody(value = "testGC1", description = "测试 GC")
    fun testScriptGC1(sender: CommandSender) {
        for (i in 0..10000) {
            plugin.scriptManager.getScript("Test.groovy").newInstance()
        }

        sender.sendMessage("Done")
    }
}