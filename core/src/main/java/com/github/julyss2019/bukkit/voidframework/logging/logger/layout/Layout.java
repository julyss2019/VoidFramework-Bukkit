package com.github.julyss2019.bukkit.voidframework.logging.logger.layout;

import com.github.julyss2019.bukkit.voidframework.logging.MessageContext;

/**
 * 日志布局
 */
public interface Layout {
    /**
     * 格式化
     * @param context 上下文
     */
    String format(MessageContext context);
}
