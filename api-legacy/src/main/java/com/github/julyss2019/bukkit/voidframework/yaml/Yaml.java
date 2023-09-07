package com.github.julyss2019.bukkit.voidframework.yaml;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Yaml 模块
 * 对 Bukkit Yaml 进行了一些简单封装
 */
public class Yaml extends Section {
    private final YamlConfiguration bukkitYaml;

    private Yaml(@NonNull YamlConfiguration yaml) {
        super(yaml);

        this.bukkitYaml = yaml;
    }

    /**
     * 保存 Yaml
     *
     * @param file 文件
     */
    public void save(@NonNull File file) {
        try {
            bukkitYaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从插件配置文件夹中的 config.yml 中读取
     *
     * @param plugin 插件
     * @return Yaml 对象
     */
    public static Yaml fromPluginConfigFile(@NonNull Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "config.yml");

        Validator.checkState(file.exists(), "file " + file.getAbsolutePath() + " not exists");
        return fromFile(file);
    }

    /**
     * 从文件中读取
     *
     * @param file 文件
     * @return Yaml 对象
     */
    public static Yaml fromFile(@NonNull File file) {
        return fromBukkitYaml(YamlConfiguration.loadConfiguration(file));
    }

    /**
     * 从 Bukkit Yaml 中读取
     *
     * @param yamlConfiguration bukkit yaml
     * @return Yaml 对象
     */
    public static Yaml fromBukkitYaml(@NonNull YamlConfiguration yamlConfiguration) {
        return new Yaml(yamlConfiguration);
    }
}
