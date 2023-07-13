package com.github.julyss2019.bukkit.voidframework.text;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;

/**
 * 文本处理器
 * 包含占位符处理和着色操作
 * 使用 Builder 模式
 */
public class TextProcessor {
    private String text;
    private boolean colored;
    private final PlaceholderContainer placeholderContainer = new PlaceholderContainer();

    /**
     * 染色
     */
    public TextProcessor colored() {
        this.colored = true;
        return this;
    }

    /**
     * 设置占位符
     *
     * @param key   键
     * @param value 值
     */
    public TextProcessor placeholder(@NonNull String key, @NonNull Object value) {
        placeholderContainer.put(key, value);
        return this;
    }

    /**
     * 设置文本
     *
     * @param text 文本
     */
    public TextProcessor text(@NonNull String text) {
        this.text = text;
        return this;
    }

    /**
     * 处理
     */
    public String process() {
        Validator.checkState(text != null, "missing text");

        String finalText = text;

        if (colored) {
            finalText = Texts.getColoredText(finalText);
        }

        return Texts.setPlaceholders(finalText, placeholderContainer);
    }
}
