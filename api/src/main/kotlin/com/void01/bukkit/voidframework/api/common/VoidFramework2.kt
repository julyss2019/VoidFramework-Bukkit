package com.void01.bukkit.voidframework.api.common

import com.void01.bukkit.voidframework.api.common.dependency.DependencyManager
import com.void01.bukkit.voidframework.api.internal.Context

object VoidFramework2 {
    private lateinit var context: Context

    fun setContext(context: Context) {
        if (VoidFramework2::context.isInitialized) {
            throw UnsupportedOperationException()
        }

        VoidFramework2.context = context
    }

    fun getDependencyManager(): DependencyManager {
        return context.dependencyManager
    }
}