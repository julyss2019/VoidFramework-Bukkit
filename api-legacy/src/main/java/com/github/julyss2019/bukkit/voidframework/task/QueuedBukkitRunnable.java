package com.github.julyss2019.bukkit.voidframework.task;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * 不会产生 “并发” 的 BukkitRunnable
 * Bukkit 自带的 runTaskTimerAsynchronously, 如果上一个任务没有完成并不会等待, 部分场景并发可能造成一些问题
 */
public abstract class QueuedBukkitRunnable extends BukkitRunnable {
    private boolean taskCompleted = true;

    public abstract void queuedRun();

    @Override
    public void run() {
        if (!taskCompleted) {
            throw new RuntimeException("the last task has not been completed");
        }

        try {
            taskCompleted = false;
            queuedRun();
        } finally {
            taskCompleted = true;
        }
    }
}
