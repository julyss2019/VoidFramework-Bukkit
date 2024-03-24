package com.void01.bukkit.voidframework.core.internal.util

import com.github.julyss2019.bukkit.voidframework.common.Messages
import org.bukkit.command.CommandSender

object MessageUtils {
    fun sendMessage(sender: CommandSender, message: String) {
        Messages.sendColoredMessage(sender, "&a[VoidFramework-Bukkit] &f$message")
    }
}