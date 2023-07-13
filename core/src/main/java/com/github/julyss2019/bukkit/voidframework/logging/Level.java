package com.github.julyss2019.bukkit.voidframework.logging;


/**
 * 日志等级
 */
public enum Level {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);

    private final int level;

    Level(int level) {
        this.level = level;
    }

    /**
     * 获取整数等级
     */
    public int getIntLevel() {
        return level;
    }
}