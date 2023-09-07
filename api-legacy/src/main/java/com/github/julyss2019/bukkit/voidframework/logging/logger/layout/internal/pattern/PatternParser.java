package com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern;

import com.github.julyss2019.bukkit.voidframework.common.Reflections;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.converter.*;
import com.github.julyss2019.bukkit.voidframework.logging.logger.layout.internal.pattern.formatter.PatternFormatter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternParser {
    public enum State {
        SIGN,
        SIGN_ESCAPE,
        LITERAL,
        KEY
    }

    private static final Map<String, Class<? extends PatternConverter>> keyToConverterClassMap = new HashMap<>();

    static {
        registerPatternConverter(DateTimePatternConverter.class);
        registerPatternConverter(LevelPatternConverter.class);
        registerPatternConverter(LiteralPatternConverter.class);
        registerPatternConverter(MessagePatternConverter.class);
        registerPatternConverter(PluginPatternConverter.class);
    }

    private static void registerPatternConverter(@NonNull Class<? extends PatternConverter> clazz) {
        ConverterKeys converterKeys = clazz.getAnnotation(ConverterKeys.class);

        if (converterKeys != null) {
            for (String key : converterKeys.value()) {
                if (keyToConverterClassMap.containsKey(key)) {
                    throw new RuntimeException(String.format("converter key already exists %s(%s)", key, clazz.getName()));
                }

                keyToConverterClassMap.put(key, clazz);
            }
        }
    }

    // [%plugin] [%level] %message
    public static List<Pattern> parse(@NonNull String patternText) {
        int len = patternText.length();
        List<Pattern> patterns = new ArrayList<>();
        State state = State.LITERAL;
        StringBuilder literalTmp = new StringBuilder(); // 普通文字缓存
        PatternConverter converterTmp;
        PatternFormatter patternFormatterTmp;

        for (int i = 0; i < len; i++) {
            char c = patternText.charAt(i);

            switch (state) {
                case LITERAL:
                    if (c == '%') {
                        state = State.SIGN;
                    }

                    break;
                case SIGN:
                    if (c == '%') {
                        state = State.SIGN_ESCAPE;
                    } else {
                        state = State.KEY;
                    }

                    break;
            }

            switch (state) {
                case LITERAL:
                    literalTmp.append(c);
                    break;
                case SIGN_ESCAPE:
                    if (literalTmp.length() != 0) {
                        patterns.add(new Pattern(null, new LiteralPatternConverter(literalTmp.toString())));
                        literalTmp.setLength(0);
                    }

                    literalTmp.append("%");
                    state = State.LITERAL;
                    break;
                case KEY:
                    if (literalTmp.length() != 0) {
                        patterns.add(new Pattern(null, new LiteralPatternConverter(literalTmp.toString())));
                        literalTmp.setLength(0);
                    }

                    String subStr = patternText.substring(i);
                    String bestKey = "";
                    Class<? extends PatternConverter> bestClass = null;

                    // 匹配 key，取最长的
                    for (Map.Entry<String, Class<? extends PatternConverter>> entry : keyToConverterClassMap.entrySet()) {
                        String convertKey = entry.getKey();
                        Class<? extends PatternConverter> convertClass = entry.getValue();

                        if (subStr.startsWith(convertKey)) {
                            if (convertKey.length() > bestKey.length()) {
                                bestKey = convertKey;
                                bestClass = convertClass;
                            }
                        }
                    }

                    if (bestClass == null) {
                        throw new RuntimeException(String.format("invalid converter near '%s'", subStr));
                    }

                    int l = patternText.indexOf("{", i);
                    PatternConverter patternConverter = Reflections.newInstance(bestClass);

                    if (l == -1) {
                        i += bestKey.length() - 1; // 考虑自增, 故 -1
                        patternConverter.setParams(null);
                    } else {
                        int r = patternText.indexOf("}", l);

                        if (r == -1) {
                            throw new RuntimeException(String.format("'%s' missing '%s'", patternText.substring(l), "}"));
                        }

                        patternConverter.setParams(patternText.substring(l + 1, r));
                        i = r;
                    }

                    patterns.add(new Pattern(null, patternConverter));
                    state = State.LITERAL;
                    break;
            }
        }

        return patterns;
    }
}
