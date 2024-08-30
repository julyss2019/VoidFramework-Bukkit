package com.void01.bukkit.voidframework.common

import java.util.concurrent.TimeUnit

@Deprecated("弃用")
class StopWatch {
    companion object {
        fun createAndStart(): StopWatch {
            return StopWatch().apply {
                start()
            }
        }
    }

    private var start = -1L

    fun start() {
        require(start == -1L) {
            "Already started"
        }

        start = System.currentTimeMillis()
    }

    @Deprecated(message = "弃用", replaceWith = ReplaceWith("elapsed(timeUnit)"))
    fun stop(): Long {
        return getElapsed(TimeUnit.MILLISECONDS)
    }

    fun getElapsed(timeUnit: TimeUnit): Long {
        require(start != -1L) {
            "Not yet started"
        }

        val tmp = TimeUnit.MILLISECONDS.convert(System.currentTimeMillis() - start, timeUnit)

        start = -1L
        return tmp
    }
}