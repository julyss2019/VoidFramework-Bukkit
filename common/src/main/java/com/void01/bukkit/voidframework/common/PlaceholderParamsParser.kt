package com.void01.bukkit.voidframework.common

object PlaceholderParamsParser {
    fun parse(paramsString: String): List<String> {
        val result = mutableListOf<String>()
        var param = ""
        var escape = false

        paramsString.forEach {
            when (it) {
                '\\' -> {
                    if (escape) {
                        param += "\\"
                        escape = false
                    } else {
                        escape = true
                    }
                }
                '_' -> {
                    if (escape) {
                        param += "_"
                        escape = false
                    } else {
                        result.add(param)
                        param = ""
                    }
                }
                else -> {
                    if (escape) {
                        escape = false
                    }

                    param += it
                }
            }
        }

        result.add(param)

        return result
    }
}