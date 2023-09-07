package com.void01.bukkit.voidframework.core.dependency;

import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class DependencyInjector {
    private static final Method method;

    static {
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static void inject(ClassLoader classLoader, File file) {
        method.setAccessible(true);
        method.invoke(classLoader, file.toURI().toURL());
        method.setAccessible(false);
    }
}
