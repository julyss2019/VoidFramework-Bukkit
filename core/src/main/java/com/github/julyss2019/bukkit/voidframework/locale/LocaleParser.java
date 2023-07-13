package com.github.julyss2019.bukkit.voidframework.locale;

import lombok.NonNull;

import java.util.Locale;

public class LocaleParser {
    /**
     * 解析 Locale
     * 规则: language_Country, 例: zh_CN
     */
    public static Locale parse(@NonNull String exp) {
        String[] array = exp.split("_");

        if (array.length != 2) {
            throw new IllegalArgumentException("illegal expression: " + exp);
        }

        String lang = array[0];
        String country = array[1];

        for (Locale availableLocale : Locale.getAvailableLocales()) {
            if (availableLocale.getLanguage().equals(lang) && availableLocale.getCountry().equals(country)) {
                return availableLocale;
            }
        }

        throw new IllegalArgumentException("illegal expression: " + exp);
    }
}
