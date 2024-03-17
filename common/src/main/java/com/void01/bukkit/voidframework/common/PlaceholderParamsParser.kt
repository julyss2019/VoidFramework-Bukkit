package com.void01.bukkit.voidframework.common

object PlaceholderParamsParser {
    fun parse(paramsString: String): List<String> {
        val result = mutableListOf<String>()
        var param = ""
        var escape = false

        paramsString.forEach {
            if (escape) {
                param += it
                escape = false
            } else {
                when (it) {
                    '\\' -> {
                        escape = true
                    }
                    '_' -> {
                        result.add(param)
                        param = ""
                    }
                    else -> {
                        param += it
                    }
                }
            }
        }

        result.add(param)
        return result
    }
}