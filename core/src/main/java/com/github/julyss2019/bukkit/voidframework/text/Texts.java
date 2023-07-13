package com.github.julyss2019.bukkit.voidframework.text;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import com.github.julyss2019.bukkit.voidframework.text.internal.PlaceholderConverter;
import lombok.NonNull;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Texts {
    /**
     * 对文本进行着色操作
     * 以 & 作为着色符
     *
     * @param text 文本
     */
    public static String getColoredText(@NonNull String text) {
        return getColoredText(text, '&');
    }

    /**
     * 对文本进行着色操作
     *
     * @param text    文本
     * @param altChar 着色符
     */
    public static String getColoredText(@NonNull String text, @NonNull Character altChar) {
        return ChatColor.translateAlternateColorCodes(altChar, text);
    }

    /**
     * 对文本列表进行着色操作
     * 以 & 作为着色符
     *
     * @param texts 文本列表
     */
    public static List<String> getColoredTexts(@NonNull List<String> texts) {
        return texts.stream().map(Texts::getColoredText).collect(Collectors.toList());
    }

    /**
     * 对文本列表进行着色操作
     *
     * @param texts   文本列表
     * @param altChar 着色符
     */
    public static List<String> getColoredTexts(@NonNull List<String> texts, @NonNull Character altChar) {
        return texts.stream().map(s -> getColoredText(s, altChar)).collect(Collectors.toList());
    }

    /**
     * 为文本列表应用占位符
     *
     * @param texts                文本
     * @param placeholderContainer 占位符容器
     */
    public static List<String> setPlaceholders(@NonNull List<String> texts, @NonNull PlaceholderContainer placeholderContainer) {
        Validator.checkNotContainsNullElement(texts, "texts cannot contains null");

        return texts.stream().map(it -> setPlaceholders(it, placeholderContainer)).collect(Collectors.toList());
    }

    /**
     * 为文本应用占位符
     *
     * @param text                 文本
     * @param placeholderContainer 占位符容器
     */
    public static String setPlaceholders(@NonNull String text, @NonNull PlaceholderContainer placeholderContainer) {
        return PlaceholderConverter.convert(text, placeholderContainer);
    }
}
