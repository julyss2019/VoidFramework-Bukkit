package com.void01.bukkit.voidframework.common.locale

import java.util.Locale

interface LocaleResource2 {
    val locale: Locale

    fun getString(path: String): String

    fun getStringList(path: String): List<String>

    fun getLocaleResource(path: String): LocaleResource2
}