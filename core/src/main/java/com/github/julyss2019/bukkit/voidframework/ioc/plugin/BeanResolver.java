package com.github.julyss2019.bukkit.voidframework.ioc.plugin;

import com.github.julyss2019.bukkit.voidframework.ioc.annotation.Component;
import com.github.julyss2019.bukkit.voidframework.ioc.annotation.PackageScan;
import com.github.julyss2019.bukkit.voidframework.ioc.bean.Bean;
import com.github.julyss2019.bukkit.voidframework.ioc.bean.SingletonBeanManager;
import lombok.NonNull;

public class BeanResolver {
    private final VoidPlugin plugin;
    private final SingletonBeanManager singletonBeanManager = new SingletonBeanManager();

    public BeanResolver(@NonNull VoidPlugin plugin) {
        this.plugin = plugin;
    }

    public void resolve() {
        singletonBeanManager.putBean(new Bean(plugin));

        // 扫描需要生成单例的类
        PackageScan annotation = plugin.getClass().getAnnotation(PackageScan.class);
        String packageName;

        if (annotation == null) {
            packageName = plugin.getClass().getPackage().getName();
        } else {
            packageName = annotation.packageName();
        }


    }
}
