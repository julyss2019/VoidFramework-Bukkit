package com.github.julyss2019.bukkit.voidframework.logging.logger.appender.internal;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

public class RollingFileNamePatternConverter {
    public enum State {
        SIGN,
        SIGN_ESCAPE,
        CONVERTER,
        PLAIN_CHAR
    }

    /**
     * %d{yyyy-MM-dd}.log %%d
     *
     */
    public static String convert(@NonNull String fileNamePattern, @NonNull LocalDate date) {
        StringBuilder processedText = new StringBuilder();
        int length = fileNamePattern.length();
        RollingFileNamePatternConverter.State state = RollingFileNamePatternConverter.State.PLAIN_CHAR;
        char converterType;

        for (int i = 0; i < length; i++) {
            char c = fileNamePattern.charAt(i);

            switch (state) {
                case PLAIN_CHAR:
                    if (c == '%') {
                        state = RollingFileNamePatternConverter.State.SIGN;
                    }

                    break;
                case SIGN:
                    if (c == '%') {
                        state = State.SIGN_ESCAPE;
                    } else {
                        state = State.CONVERTER;
                    }

                    break;
                default:
                    state = State.PLAIN_CHAR;
            }

            switch (state) {
                case PLAIN_CHAR:
                    processedText.append(c);
                    break;
                case SIGN_ESCAPE:
                    processedText.append("%");
                    break;
                case CONVERTER:
                    converterType = c;

                    if ('d' == converterType) {
                        int l = fileNamePattern.indexOf("{", i + 1);

                        if (l == -1) {
                            throw new RuntimeException(String.format("'%s' missing '%s'", processedText, "{"));
                        }

                        int r = fileNamePattern.indexOf("}", l + 1);

                        if (r == -1) {
                            throw new RuntimeException(String.format("'%s' missing '%s'", processedText, "}"));
                        }

                        String params = fileNamePattern.substring(l + 1, r);

                        processedText.append(date.format(DateTimeFormatter.ofPattern(params)));
                        i = r;
                    } else {
                        throw new RuntimeException("invalid converter: '" + c + "'");
                    }

                    state = State.PLAIN_CHAR;
                    break;
            }
        }

        return processedText.toString();
    }
}
