package com.github.julyss2019.bukkit.voidframework.ioc.bean;

import com.google.common.collect.Maps;
import lombok.NonNull;

import java.util.Map;

public class SingletonBeanManager {
    private final Map<Class<?>, Object> beanMap = Maps.newHashMap();

    public void putBean(@NonNull Bean bean) {
        beanMap.put(bean.getObject().getClass(), bean);
    }

    public Bean getBean(@NonNull Class<?> clazz) {
        return (Bean) beanMap.get(clazz);
    }
}
