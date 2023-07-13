package com.github.julyss2019.bukkit.voidframework.common;

import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class Bungees {
    /**
     * 连接服务器
     */
    public static void connectServer(@NonNull Player player, @NonNull String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);
        sendPluginMessage(player, out);
    }

    /**
     * 广播消息
     */
    public static void broadcast(@NonNull Player player, @NonNull String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Message");
        out.writeUTF("ALL");
        out.writeUTF(message);
        sendPluginMessage(player, out);
    }

    private static void sendPluginMessage(@NonNull Player player, @NonNull ByteArrayDataOutput output) {
        player.sendPluginMessage(VoidFrameworkPlugin.getInst(), "BungeeCord", output.toByteArray());
    }
}
