package com.void01.bukkit.voidframework.api.common.library;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * 孤立的加载器
 * 打破了双亲委派机制
 */
public class IsolatedClassLoader extends URLClassLoader {
    public IsolatedClassLoader(ClassLoader parent) {
        super(new URL[]{}, parent);
    }

    @SneakyThrows
    public void addURL(@NonNull Path path) {
        addURL(path.toUri().toURL());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (name.startsWith("java.") || name.startsWith("org.bukkit") || name.startsWith("io.izzel.arclight")) {
                return super.loadClass(name);
            }

            Class<?> loadedClass = findLoadedClass(name);

            if (loadedClass != null) {
                return loadedClass;
            }

            try {
                loadedClass = findClass(name);
            } catch (ClassNotFoundException ignored) {
            }

            if (loadedClass == null) {
                try {
                    loadedClass = super.loadClass(name);
                } catch (ClassNotFoundException ignored) {
                }
            }

            if (loadedClass == null) {
                throw new ClassNotFoundException(name);
            }

            return loadedClass;
        }
    }
}
