package com.github.julyss2019.bukkit.voidframework.test;

import org.junit.Assert;

public class Benchmark {
    public static void test(Runnable runnable, long timeout) {
        long beginningTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        long cost = endTime - beginningTime;

        Assert.assertTrue("超时: " + cost + "/" + timeout, cost < timeout);

    }
}
