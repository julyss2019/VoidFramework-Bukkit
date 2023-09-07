package com.github.julyss2019.bukkit.voidframework.locale.resource;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.DefaultValue;
import com.github.julyss2019.bukkit.voidframework.yaml.Yaml;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class YamlLocaleResource extends BaseLocaleResource {
    /**
     * 获取默认的本土化资源文件夹
     * @param plugin 插件
     * @return
     */
    public static File getDefaultLocaleFolder(@NonNull Plugin plugin) {
        return new File(plugin.getDataFolder(), "locales");
    }

    /**
     * 载入一个本土化资源
     * 以 lang_country.yml 载入文件，例：zh_CN.yml
     * @param locale 本土化
     * @param resourceFolder 资源文件夹
     */
    public static YamlLocaleResource fromFolder(@NonNull Locale locale, @NonNull File resourceFolder) {
        return new YamlLocaleResource(locale, resourceFolder);
    }

    /**
     * 载入一个本土化资源
     * 以插件文件夹下的 locale 文件夹作为资源文件夹
     * @param locale 本土化
     * @param plugin 插件
     */
    public static YamlLocaleResource fromPluginLocaleFolder(@NonNull Locale locale, @NonNull Plugin plugin) {
        return new YamlLocaleResource(locale, new File(plugin.getDataFolder(), "locales"));
    }

    private final File file;
    private Yaml yaml;

    private YamlLocaleResource(@NonNull Locale locale, @NonNull File resourcesFolder) {
        super(locale);

        this.file = new File(resourcesFolder, locale.getLanguage() + "_" + locale.getCountry().toUpperCase() + ".yml");

        load();
    }

    private void load() {
        if (!file.exists()) {
            throw new RuntimeException("locale resource file not found: " + file.getAbsolutePath());
        }

        this.yaml = Yaml.fromFile(file);
    }

    @Override
    public LocaleResource getLocalResource(@NonNull String key) {
        if (!yaml.contains(key)) {
            throw new LocaleResourceNotExistsException(key);
        }

        return new LocaleResource() {
            final YamlLocaleResource parent = YamlLocaleResource.this;

            private String convertKey(String key1) {
                return key + "." + key1;
            }

            @Override
            public LocaleResource getLocalResource(@NonNull String key) {
                return parent.getLocalResource(convertKey(key));
            }

            @Override
            public List<String> getStringList(@NonNull String key, List<String> def) {
                return parent.getStringList(convertKey(key), def);
            }

            @Override
            public String getString(@NonNull String key, String def) {
                return parent.getString(convertKey(key), def);
            }

            @Override
            public Locale getLocale() {
                return parent.getLocale();
            }

            @Override
            public void reload() {
                parent.reload();
            }
        };
    }

    @Override
    public List<String> getStringList(@NonNull String key, @Nullable List<String> def) {
        return yaml.getStringList(key, DefaultValue.of(def));
    }

    @Override
    public String getString(@NonNull String key, String def) {
        return yaml.getString(key, DefaultValue.of(def));
    }

    @Override
    public void reload() {
        load();
    }
}
