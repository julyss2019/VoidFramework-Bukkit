package com.void01.bukkit.voidframework.api.common.library;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 孤立的加载器
 * 打破了双亲委派机制
 */
public class IsolatedClassLoader extends URLClassLoader {
    private  ClassLoader parent;
    private final List<String> whitelistClasses;

    public IsolatedClassLoader( ClassLoader parent) {
        this(parent, new ArrayList<>());

    }

    public IsolatedClassLoader(ClassLoader parent, List<String> whitelistClasses) {
        super(new URL[]{}, null);

        this.whitelistClasses = new ArrayList<>(whitelistClasses);

        whitelistClasses.add("java");
        whitelistClasses.add("org.bukkit");
        whitelistClasses.add("io.izzel.arclight");

        this.parent = parent;
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
                loadedClass = findClass(name);
            } catch (ClassNotFoundException ignored) {
            }

            if (loadedClass == null && parent != null) {
                try {
                    loadedClass = parent.loadClass(name);
                } catch (ClassNotFoundException ignored) {
                }
            }

            if (loadedClass == null ) {
                try {
                    loadedClass = ClassLoader.getSystemClassLoader().loadClass(name);
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
