package com.void01.bukkit.voidframework.api.common.dependency;

import lombok.NonNull;

public class Repository {
    public static final Repository ALIYUN_REPOSITORY = new Repository("https://maven.aliyun.com/repository/public");
    public static final Repository CENTRAL_REPOSITORY = new Repository("https://repo.maven.apache.org/maven2");

    private final String url;

    public Repository(@NonNull String url) {
        this.url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public String getUrl() {
        return url;
    }
}
