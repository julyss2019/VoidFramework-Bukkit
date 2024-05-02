package com.void01.bukkit.voidframework.common


enum class TimeUnit {
    SECONDS {
        override fun toMillis(source: Long): Long {
            return source * 1000L
        }

        override fun toSeconds(source: Long): Long {
            return source
        }

        override fun toTicks(source: Long): Long {
            return source * 20L
        }

        override fun convert(source: Long, sourceUnit: TimeUnit): Long {
            return sourceUnit.toSeconds(source)
        }
    },
    TICKS {
        override fun toMillis(source: Long): Long {
            return source * 50L
        }

        override fun toSeconds(source: Long): Long {
            return (source * 0.05).toLong()
        }

        override fun toTicks(source: Long): Long {
            return source
        }

        override fun convert(source: Long, sourceUnit: TimeUnit): Long {
            return sourceUnit.toMillis(source)
        }
    },
    MILLISECONDS {
        override fun toMillis(source: Long): Long {
            return source
        }

        override fun toSeconds(source: Long): Long {
            return source / 1000L
        }

        override fun toTicks(source: Long): Long {
            return source / 50L
        }

        override fun convert(source: Long, sourceUnit: TimeUnit): Long {
            return sourceUnit.toMillis(source)
        }
    };

    abstract fun toMillis(source: Long): Long

    abstract fun toSeconds(source: Long): Long

    abstract fun toTicks(source: Long): Long

    abstract fun convert(source: Long, sourceUnit: TimeUnit): Long
}