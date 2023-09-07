package com.github.julyss2019.bukkit.voidframework.internal.task;

import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleAppenderFlushTask extends BukkitRunnable {
    private final LegacyVoidFrameworkPlugin plugin;
    private final Queue<String> consoleMessageQueue = new ConcurrentLinkedQueue<>();

    public ConsoleAppenderFlushTask(@NonNull LegacyVoidFrameworkPlugin plugin) {
        this.plugin = plugin;
    }

    public void addConsoleMessage(@NonNull String message) {
        consoleMessageQueue.add(message);
    }

    @Override
    public void run() {
        String message = consoleMessageQueue.poll();

        if (message == null) {
            return;
        }

        Bukkit.getConsoleSender().sendMessage(message);
    }
}
