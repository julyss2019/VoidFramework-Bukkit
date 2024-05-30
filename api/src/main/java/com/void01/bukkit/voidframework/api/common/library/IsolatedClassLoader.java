package com.void01.bukkit.voidframework.api.common.library;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 孤立的加载器
 * 打破了双亲委派机制
 */
public class IsolatedClassLoader extends URLClassLoader {
    private final List<String> whitelistClasses;

    public IsolatedClassLoader(ClassLoader parent) {
        this(parent, new ArrayList<>());
    }

    public IsolatedClassLoader(ClassLoader parent, List<String> whitelistClasses) {
        super(new URL[]{}, parent);

        this.whitelistClasses = new ArrayList<>(whitelistClasses);

        whitelistClasses.add("java");
        whitelistClasses.add("org.bukkit");
        whitelistClasses.add("io.izzel.arclight");
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public List<String> getWhitelistClasses() {
        return Collections.unmodifiableList(whitelistClasses);
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
        synchronized (getClassLoadingLock(name)) {
            for (String whitelistClass : whitelistClasses) {
                if (name.startsWith(whitelistClass)) {
                    return super.loadClass(name);
                }
            }

            Class<?> loadedClass = findLoadedClass(name);

            if (loadedClass != null) {
                return loadedClass;
            }

            try {
                return findClass(name);
            } catch (ClassNotFoundException | NoClassDefFoundError exception) {
                return super.loadClass(name);
            }
        }
    }
}
