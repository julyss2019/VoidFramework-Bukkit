package com.void01.bukkit.voidframework.core;

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import com.void01.bukkit.voidframework.api.common.VoidFramework2;
import com.void01.bukkit.voidframework.api.common.datasource.DataSourceManager;
import com.void01.bukkit.voidframework.api.common.groovy.GroovyBinding;
import com.void01.bukkit.voidframework.api.common.groovy.GroovyConfig;
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager;
import com.void01.bukkit.voidframework.api.common.library.Library;
import com.void01.bukkit.voidframework.api.common.library.LibraryManager;
import com.void01.bukkit.voidframework.api.common.library.Repository;
import com.void01.bukkit.voidframework.api.internal.Context;
import com.void01.bukkit.voidframework.core.datasource.DataSourceManagerImpl;
import com.void01.bukkit.voidframework.core.groovy.GroovyManagerImpl;
import com.void01.bukkit.voidframework.core.library.LibraryManagerImpl;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;

@CommandMapping(value = "void-framework", permission = "void-framework.admin")
public class VoidFrameworkPlugin extends JavaPlugin implements Context {
    private LegacyVoidFrameworkPlugin legacy;
    @Getter
    private final LibraryManager libraryManager;
    @Getter
    private DataSourceManager dataSourceManager;
    @Getter
    private GroovyManager groovyManager;

    // 提前加载
    public VoidFrameworkPlugin() {
        this.libraryManager = new LibraryManagerImpl(this);
        libraryManager.load(Library.Builder
                .create()
                .setClassLoaderByBukkitPlugin(this)
                .setDependencyByGradleStyleExpression("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
                .addRepositories(Repository.ALIYUN, Repository.CENTRAL)
                .addSafeRelocation("_kotlin_", "_com.void01.bukkit.voidframework.core.libs.kotlin_")
                .build()
        );
        VoidFramework2.INSTANCE.setContext(this);
    }

    @Override
    public void onEnable() {
        this.dataSourceManager = new DataSourceManagerImpl();
        this.groovyManager = new GroovyManagerImpl(this);

        // 预加载，第一次加载需要时间
        groovyManager.eval("1+1");

        legacy = new LegacyVoidFrameworkPlugin(this);
        legacy.onEnable();
    }

    @Override
    public void onDisable() {
        legacy.onDisable();
    }
}
