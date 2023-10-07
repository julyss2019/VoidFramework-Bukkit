package com.void01.bukkit.voidframework.common;

import sun.misc.Unsafe;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class UrlClassLoaderModifier {
    private static final Method addUrlMethod;
    private static final Unsafe unsafe;
    private static final MethodHandles.Lookup trustedLookup;

    static {
        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");

            theUnsafeField.setAccessible(true);
            unsafe = (Unsafe) theUnsafeField.get(null);

            // 参考：https://sourcegraph.com/github.com/alibaba/fastjson2/-/blob/core/src/main/java/com/alibaba/fastjson2/util/JDKUtils.java?L219:13
            Field lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP"); // 绕过 Java 检查，这个变量权限是 TRUSTED
            Object lookupBase = unsafe.staticFieldBase(lookupField);
            long lookupOffset = unsafe.staticFieldOffset(lookupField);
            trustedLookup = (MethodHandles.Lookup) unsafe.getObject(lookupBase, lookupOffset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUrl(ClassLoader classLoader, File file) {
        URL url;

        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            // JDK 8 or higher
            addUrlMethod.setAccessible(true);
            addUrlMethod.invoke(classLoader, url);
            addUrlMethod.setAccessible(false);
        } catch (Exception ex) {
            // JDK 9 or higher
            try {
                Field ucpField = URLClassLoader.class.getDeclaredField("ucp"); // ucp = URLClassPath
                Object ucp = unsafe.getObject(classLoader, unsafe.objectFieldOffset(ucpField)); // 通过 Unsafe 绕过检查，不用添加 --add-opens=java.base/java.net=ALL-UNNAMED 了
                MethodHandle methodHandle = trustedLookup.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, URL.class));

                methodHandle.invoke(ucp, url);
            } catch (Throwable ex1) {
                ex.printStackTrace();
                ex1.printStackTrace();
                throw new RuntimeException("inject failed");
            }
        }
    }
}
