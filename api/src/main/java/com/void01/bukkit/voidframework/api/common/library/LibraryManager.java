package com.void01.bukkit.voidframework.api.common.library;

import com.void01.bukkit.voidframework.api.common.library.relocation.Relocation;
import lombok.NonNull;

import java.util.List;

public interface LibraryManager {
    @Deprecated
    void load(@NonNull Library library);

    void loadDependency(@NonNull Dependency dependency,
                        @NonNull ClassLoader classLoader,
                        @NonNull List<Repository> repositories,
                        @NonNull List<Relocation> relocations,
                        boolean loadRecursively);
}
