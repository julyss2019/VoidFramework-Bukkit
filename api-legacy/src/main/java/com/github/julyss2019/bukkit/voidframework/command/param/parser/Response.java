package com.github.julyss2019.bukkit.voidframework.command.param.parser;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import lombok.NonNull;

public class Response {
    private final Object object;
    private final String errorMessage;

    private Response(Object object, String errorMessage) {
        this.object = object;
        this.errorMessage = errorMessage;
    }

    public static Response success(@NonNull Object object) {
        return new Response(object, null);
    }

    public static Response failure(@Nullable String errorMessage) {
        return new Response(null, errorMessage);
    }

    public Object getObject() {
        return object;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
