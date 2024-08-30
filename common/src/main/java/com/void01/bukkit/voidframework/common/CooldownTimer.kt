package com.void01.bukkit.voidframework.common


@Deprecated("弃用")
class CooldownTimer {
    /**
     * 是否已经冷却完毕
     */
    val isFinished: Boolean get() = System.currentTimeMillis() > finishTimeMillis

    /**
     * 冷却结束时间
     */
    var finishTimeMillis: Long = -1L
        private set

    /**
     * 增加冷却时间
     */
    fun addCooldown(cooldown: Long, timeUnit: TimeUnit) {
        if (isFinished) {
            finishTimeMillis = System.currentTimeMillis()
        }

        finishTimeMillis += timeUnit.toMillis(cooldown)
    }

    /**
     * 获取冷却时间
     */
    fun getCooldown(timeUnit: TimeUnit): Long {
        if (finishTimeMillis == -1L) {
            return 0L
        }

        val remaining = (finishTimeMillis - System.currentTimeMillis()).coerceAtLeast(0)

        // MILLS -> ?
        return timeUnit.convert(remaining, TimeUnit.MILLISECONDS)
    }
}