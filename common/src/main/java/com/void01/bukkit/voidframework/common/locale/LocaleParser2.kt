package com.void01.bukkit.voidframework.common.locale

import java.util.*

object LocaleParser2 {
    /**
     * 解析 Locale
     * 规则: language_Country, 例: zh_CN
     */
    fun parse(exp: String): Locale {
        val array = exp.split("_")

        require(array.size == 2) { "Illegal expression: $exp" }

        val lang = array[0]
        val country = array[1]

        for (availableLocale in Locale.getAvailableLocales()) {
            if (availableLocale.language == lang && availableLocale.country == country) {
                return availableLocale
            }
        }

        throw IllegalArgumentException("Illegal expression: $exp")
    }
}
