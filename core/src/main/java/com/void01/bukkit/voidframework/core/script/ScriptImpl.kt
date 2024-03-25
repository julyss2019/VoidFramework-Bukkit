package com.void01.bukkit.voidframework.core.script

import com.void01.bukkit.voidframework.api.common.script.Script
import java.nio.file.Path

class ScriptImpl(override val path: Path, override val relativePath: String, override val clazz: Class<*>) : Script {
    override fun newInstance(): Any {
        return clazz.newInstance()
    }

    override fun <T> newInstance(clazz: Class<T>): T {
        return clazz.newInstance() as T
    }

    override fun toString(): String {
        return "Script(relativePath='$relativePath', clazz=$clazz)"
    }
}