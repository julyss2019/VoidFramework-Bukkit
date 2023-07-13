package com.github.julyss2019.bukkit.voidframework.text.internal;

import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import lombok.NonNull;

/**
 * 占位符转换器
 */
public class PlaceholderConverter {
    public enum State {
        LITERAL, // 普通字符
        SIGN, // 占位符标记
        SIGN_ESCAPE, // 占位符转义
        PLACEHOLDER
    }

    /**
     * 占位符转换器
     * ${key} 支持转义 $${key}
     * @param text 文本
     * @param placeholderContainer 占位符容器
     */
    public static String convert(@NonNull String text, @NonNull PlaceholderContainer placeholderContainer) {
        StringBuilder processedText = new StringBuilder();
        int length = text.length();
        State state = State.LITERAL;

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            // 状态
            switch (state) {
                case LITERAL:
                    if (c == '$') {
                        state = State.SIGN;
                    }

                    break;
                case SIGN:
                    if (c == '$') {
                        state = State.SIGN_ESCAPE;
                    } else {
                        state = State.PLACEHOLDER;
                    }

                    break;
            }

            // 处理
            switch (state) {
                case LITERAL:
                    processedText.append(c);
                    break;
                case SIGN_ESCAPE:
                    processedText.append("$");
                    state = State.LITERAL;
                    break;
                case PLACEHOLDER:
                    int l = text.indexOf("{", i);

                    if (l == -1) {
                        throw new RuntimeException(String.format("'%s' missing '%s'", text, "{"));
                    }

                    int r = text.indexOf("}", l);

                    if (r == -1) {
                        throw new RuntimeException(String.format("'%s' missing '%s'", text, "}"));
                    }

                    String key = text.substring(l + 1, r);

                    processedText.append(placeholderContainer.getValue(key));
                    state = State.LITERAL;
                    i = r;
                    break;
            }
        }

        return processedText.toString();
    }
}
