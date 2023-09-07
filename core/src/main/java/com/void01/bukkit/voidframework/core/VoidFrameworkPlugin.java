package com.void01.bukkit.voidframework.core;

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import com.void01.bukkit.voidframework.api.common.VoidFramework2;
import com.void01.bukkit.voidframework.api.common.dependency.Dependency;
import com.void01.bukkit.voidframework.api.common.dependency.DependencyManager;
import com.void01.bukkit.voidframework.api.common.dependency.ShadowSafeRelocation;
import com.void01.bukkit.voidframework.api.internal.Context;
import com.void01.bukkit.voidframework.core.dependency.DependencyManagerImpl;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
public class VoidFrameworkPlugin extends JavaPlugin implements Context {
    private LegacyVoidFrameworkPlugin legacy;
    @Getter
    private DependencyManager dependencyManager;

    // 提前加载
    public VoidFrameworkPlugin() {
        this.dependencyManager = new DependencyManagerImpl(this);
        dependencyManager.loadDependency(
                this,
                Dependency.createByGradleExpression("org.jetbrains.kotlin:kotlin-stdlib:1.9.10"),
                Collections.singletonList(ShadowSafeRelocation.createSafely("_kotlin_", "_kotlin_1_9_10_"))
        );
        VoidFramework2.INSTANCE.setContext(this);
    }

    @Override
    public void onEnable() {
        legacy = new LegacyVoidFrameworkPlugin(this);
        legacy.onEnable();
    }

    @Override
    public void onDisable() {
        legacy.onDisable();
    }

}
