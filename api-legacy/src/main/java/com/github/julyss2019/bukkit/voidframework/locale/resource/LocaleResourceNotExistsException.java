package com.github.julyss2019.bukkit.voidframework.locale.resource;

public class LocaleResourceNotExistsException extends RuntimeException {
    public LocaleResourceNotExistsException(String message) {
        super(String.format("locale '%s' not exists", message));
    }
}
