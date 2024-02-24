package com.void01.bukkit.voidframework.common

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

    fun stop(): Long {
        require(start != -1L) {
            "Not yet started"
        }

        return System.currentTimeMillis() - start
    }
}