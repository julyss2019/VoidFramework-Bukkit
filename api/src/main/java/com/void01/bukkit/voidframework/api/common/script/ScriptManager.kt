package com.void01.bukkit.voidframework.api.common.script

interface ScriptManager {
    val scripts: List<Script>

    fun getScript(relativePath: String): Script

    fun getScriptOrNull(relativePath: String): Script?
}