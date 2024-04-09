package com.github.julyss2019.bukkit.voidframework.common;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import lombok.NonNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflections {
    public static Object convertArray(@NonNull Object[] originalArray, @NonNull Class<?> targetType) {
        Object convertedArray = Array.newInstance(targetType, originalArray.length);

        for (int i = 0; i < originalArray.length; i++) {
            Array.set(convertedArray, i, originalArray[i]);
        }

        return convertedArray;
    }

    public static <T> T[] getEnums(@NonNull Class<T> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("class is not enum");
        }

        Object[] enumConstants = clazz.getEnumConstants();

        return (T[]) enumConstants;
    }

    public static <T> T getEnum(@NonNull Class<T> clazz, @NonNull String name) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException(clazz.getName() + " is not a Enum");
        }

        for (Object enumConstant : clazz.getEnumConstants()) {
            Method nameMethod = null;

            try {
                nameMethod = clazz.getMethod("name");

                nameMethod.setAccessible(true);

                if (nameMethod.invoke(enumConstant).equals(name)) {
                    return (T) enumConstant;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            } finally {
                if (nameMethod != null) {
                    nameMethod.setAccessible(false);
                }
            }
        }

        return null;
    }

    public static Method getMethod(@NonNull Class<?> clazz, @NonNull String methodName, @NonNull Class<?>... parameters) {
        try {
            return clazz.getMethod(methodName, parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getDeclaredMethod(@NonNull Class<?> clazz, @NonNull String methodName, @NonNull Class<?>... parameters) {
        try {
            return clazz.getDeclaredMethod(methodName, parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(@NonNull Object obj, @NonNull String methodName, @NonNull Object... parameters) {
        try {
            Class<?>[] parameterTypes = new Class[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getClass();
            }

            return getDeclaredMethod(obj.getClass(), methodName, parameterTypes).invoke(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(@NonNull Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDeclaredFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName, @NonNull Object classInst, @Nullable Object fieldValue) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);

            declaredField.setAccessible(true);
            declaredField.set(classInst, fieldValue);
            declaredField.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getDeclaredFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName, @NonNull Object classInst) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);

            declaredField.setAccessible(true);

            Object tmp = declaredField.get(classInst);

            declaredField.setAccessible(false);

            return tmp;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName, @NonNull Object classInst, @Nullable Object fieldValue) {
        try {
            Field field = clazz.getField(fieldName);

            field.setAccessible(true);
            field.set(classInst, fieldValue);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(@NonNull Class<?> clazz, @NonNull String fieldValue, @NonNull Object classInst) {
        try {
            Field field = clazz.getField(fieldValue);

            field.setAccessible(true);

            Object tmp = field.get(classInst);

            field.setAccessible(false);
            return tmp;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
