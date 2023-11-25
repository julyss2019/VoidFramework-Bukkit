package com.void01.bukkit.voidframework.api.common.library;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 孤立的加载器
 * 打破了双亲委派机制
 */
public class IsolatedClassLoader extends URLClassLoader {
    public IsolatedClassLoader(ClassLoader parent) {
        super(new URL[]{}, parent);
    }

    @SneakyThrows
    public void addURL(@NonNull File file) {
        addURL(file.toURI().toURL());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);

/*        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);

            if (loadedClass != null) {
                return loadedClass;
            }

            return super.loadClass(name);
        }*/
    }
}
