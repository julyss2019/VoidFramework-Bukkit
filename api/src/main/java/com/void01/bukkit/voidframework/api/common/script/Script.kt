package com.void01.bukkit.voidframework.api.common.script

import java.nio.file.Path

@Deprecated("弃用")
interface Script {
    val path: Path
    val relativePath: String
    val clazz: Class<*>

    fun newInstance(): Any
}