package com.github.julyss2019.bukkit.voidframework.common;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import com.github.julyss2019.bukkit.voidframework.text.Texts;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Messages {
    public static void broadcastColoredMessage(@Nullable String message) {
        Bukkit.broadcastMessage(Texts.getColoredText(message));
    }

    /**
     * 发送消息（着色）
     */
    @Deprecated
    public static void sendMessage(@Nullable CommandSender sender, @NonNull String message) {
        sendColoredMessage(sender, message);
    }

    /**
     * 发送消息（着色）
     */
    public static void sendColoredMessage(@NonNull CommandSender sender, @NonNull String message) {
        sender.sendMessage(Texts.getColoredText(message));
    }

    public static void sendColoredPlaceholderMessage(@NonNull CommandSender sender, @NonNull String message, @NonNull PlaceholderContainer placeholderContainer) {
        sendColoredMessage(sender, Texts.setPlaceholders(message, placeholderContainer));
    }
}
