package com.github.julyss2019.bukkit.voidframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 有效物品的判断标准：非空气，非 null
 */
@Target(ElementType.PARAMETER)
public @interface ValidItem {
}
