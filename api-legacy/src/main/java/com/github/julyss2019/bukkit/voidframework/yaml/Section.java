package com.github.julyss2019.bukkit.voidframework.yaml;


import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.Serializable;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal.EnumSerializable;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal.EnumSetSerializable;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal.ItemStackSerializable;
import com.github.julyss2019.bukkit.voidframework.yaml.serializable.internal.ShortSerializable;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Section 类对 Bukkit Section 进行了以下扩展：
 * 1. 多路径支持
 * 2. 默认值支持
 * 3. 找不到值自动抛异常
 * 4. Enum/Enum Set/Short 解析
 * 5. 支持可读性更高的 ItemStack/Location 配置解析
 * 6. 可自定义的序列化
 */
public class Section {
    public interface Parser<T> {
        T parse(@NonNull String path);
    }

    private final ConfigurationSection bukkitSection;
    private final String currentPath;

    protected Section(@NonNull ConfigurationSection bukkitSection) {
        this.bukkitSection = bukkitSection;
        this.currentPath = bukkitSection.getCurrentPath();
    }

    public static Section getEmptySection() {
        return new Section(new MemoryConfiguration());
    }

    /**
     * 获取节点名
     */
    public String getName() {
        return this.bukkitSection.getName();
    }

    /**
     * 获取当前路径
     */
    public String getCurrentPath() {
        return currentPath;
    }

    /**
     * 从 Bukkit 加载配置节点
     */
    public static Section fromBukkitSection(@NonNull ConfigurationSection bukkitSection) {
        return new Section(bukkitSection);
    }

    /**
     * 获取原生 Bukkit Section
     */
    public ConfigurationSection getBukkitSection() {
        return this.bukkitSection;
    }

    /**
     * 获取配置节点，若不存在则创建
     *
     * @param path 路径
     */
    public Section getOrCreateSection(@NonNull String path) {
        if (!contains(path)) {
            bukkitSection.createSection(path);
        }

        return getSection(path);
    }

    /**
     * 获取 Section
     *
     * @param path 路径
     */
    public Section getSection(@NonNull String path) {
        return getSection(Paths.of(path), null);
    }

    /**
     * 获取 Section
     *
     * @param path 路径
     * @param def  默认值
     */
    public Section getSection(@NonNull String path, @Nullable DefaultValue<Section> def) {
        return getSection(Paths.of(path), def);
    }

    /**
     * 获取 Section
     *
     * @param paths 路径
     * @param def   默认值
     */
    public Section getSection(@NonNull Paths paths, @Nullable DefaultValue<Section> def) {
        return parse(paths, path -> {
            ConfigurationSection bukkitSection = Section.this.bukkitSection.getConfigurationSection(path);

            if (bukkitSection == null) {
                return null;
            }

            return Section.fromBukkitSection(bukkitSection);
        }, def);
    }

    /**
     * 判断是否包含路径
     *
     * @param path 路径
     */
    public boolean contains(@NonNull String path) {
        return bukkitSection.contains(path);
    }

    /**
     * 判断是否包含路径
     *
     * @param paths 路径
     */
    public boolean contains(@NonNull Paths paths) {
        for (String path : paths.getPaths()) {
            if (this.bukkitSection.contains(path)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取当前路径下的键列表
     */
    public Set<String> getSubKeys() {
        return bukkitSection.getKeys(false);
    }

    /**
     * 获取当前路径下的所有子节点
     */
    public Set<Section> getSubSections() {
        Set<Section> sections = new HashSet<>();

        this.bukkitSection.getKeys(false).forEach(key -> {
            ConfigurationSection configurationSection = this.bukkitSection.getConfigurationSection(key);

            if (configurationSection != null) {
                sections.add(new Section(configurationSection));
            }
        });

        return sections;
    }

    /**
     * 获取 ItemStack（使用使用框架自带的 ItemStackSerializable）
     *
     * @param path 路径
     */
    public ItemStack getItemStack(@NonNull String path) {
        return getItemStack(Paths.of(path), null);
    }

    /**
     * 获取 ItemStack
     * 使用框架自带的 ItemStackSerializable 进行解析
     *
     * @param path 路径
     * @param def  默认值
     */
    public ItemStack getItemStack(@NonNull String path, @Nullable DefaultValue<ItemStack> def) {
        return getItemStack(Paths.of(path), def);
    }

    /**
     * 获取 ItemStack
     * 使用框架自带的 ItemStackSerializable 进行解析
     *
     * @param paths 路径
     * @param def   默认值
     */
    public ItemStack getItemStack(@NonNull Paths paths, @Nullable DefaultValue<ItemStack> def) {
        return getBySerializable(paths, ItemStackSerializable.getInstance(), def);
    }

    /**
     * 获取 Integer List
     *
     * @param path 路径
     */
    public List<Integer> getIntegerList(@NonNull String path) {
        return getIntegerList(Paths.of(path), DefaultValue.of(Collections.emptyList()));
    }

    /**
     * 获取 Integer List
     *
     * @param path 路径
     * @param def  默认值
     */
    public List<Integer> getIntegerList(@NonNull String path, @Nullable DefaultValue<List<Integer>> def) {
        return getIntegerList(Paths.of(path), def);
    }

    /**
     * 获取 Integer List
     *
     * @param paths 路径
     * @param def   默认值
     */
    public List<Integer> getIntegerList(@NonNull Paths paths, @Nullable DefaultValue<List<Integer>> def) {
        return parse(paths, bukkitSection::getIntegerList, def);
    }

    /**
     * 获取 String List
     *
     * @param path 路径
     */
    public List<String> getStringList(@NonNull String path) {
        return getStringList(Paths.of(path), null);
    }

    /**
     * 获取 String List
     *
     * @param path 路径
     * @param def  默认值
     */
    public List<String> getStringList(@NonNull String path, @Nullable DefaultValue<List<String>> def) {
        return getStringList(Paths.of(path), def);
    }

    /**
     * 获取 String List
     *
     * @param paths 路径
     * @param def   默认值
     */
    public List<String> getStringList(@NonNull Paths paths, @Nullable DefaultValue<List<String>> def) {
        return parse(paths, bukkitSection::getStringList, def);
    }

    /**
     * 获取 Short
     *
     * @param path 路径
     */
    public short getShort(@NonNull String path) {
        return getShort(Paths.of(path), null);
    }

    /**
     * 获取 Short
     *
     * @param path 路径
     * @param def  默认值
     */
    public short getShort(@NonNull String path, @Nullable DefaultValue<Short> def) {
        return getShort(Paths.of(path), def);
    }

    /**
     * 获取 Short
     *
     * @param paths 路径
     * @param def   默认值
     */
    public short getShort(@NonNull Paths paths, @Nullable DefaultValue<Short> def) {
        return getBySerializable(paths, ShortSerializable.getInstance(), def);
    }

    /**
     * 获取 Integer
     *
     * @param path 路径
     */
    public int getInt(@NonNull String path) {
        return getInt(Paths.of(path), null);
    }

    /**
     * 获取 Integer
     *
     * @param path 路径
     * @param def  默认值
     */
    public int getInt(@NonNull String path, @Nullable DefaultValue<Integer> def) {
        return getInt(Paths.of(path), def);
    }

    /**
     * 获取 Integer
     *
     * @param paths 路径
     * @param def   默认值
     */
    public int getInt(@NonNull Paths paths, @Nullable DefaultValue<Integer> def) {
        return parse(paths, bukkitSection::getInt, def);
    }

    /**
     * 获取 Double
     *
     * @param path 路径
     */
    public double getDouble(@NonNull String path) {
        return getDouble(Paths.of(path), null);
    }

    /**
     * 获取 Double
     *
     * @param path 路径
     * @param def  默认值
     */
    public double getDouble(@NonNull String path, @Nullable DefaultValue<Double> def) {
        return getDouble(Paths.of(path), def);
    }

    /**
     * 获取 Double
     *
     * @param paths 路径
     * @param def   默认值
     */
    public double getDouble(@NonNull Paths paths, @Nullable DefaultValue<Double> def) {
        return parse(paths, bukkitSection::getDouble, def);
    }

    /**
     * 获取 Float
     *
     * @param path 路径
     */
    public float getFloat(@NonNull String path) {
        return getFloat(Paths.of(path), null);
    }

    /**
     * 获取 Float
     *
     * @param path 路径
     * @param def  默认值
     */
    public float getFloat(@NonNull String path, @Nullable DefaultValue<Float> def) {
        return getFloat(Paths.of(path), def);
    }

    /**
     * 获取 Float
     *
     * @param paths 路径
     * @param def   默认值
     */
    public float getFloat(@NonNull Paths paths, @Nullable DefaultValue<Float> def) {
        return parse(paths, path -> (float) bukkitSection.getDouble(path), def);
    }

    /**
     * 获取 String
     *
     * @param path 路径
     */
    public String getString(@NonNull String path) {
        return getString(Paths.of(path), null);
    }

    /**
     * 获取 String
     *
     * @param path 路径
     * @param def  默认值
     */
    public String getString(@NonNull String path, @Nullable DefaultValue<String> def) {
        return getString(Paths.of(path), def);
    }

    /**
     * 获取 String
     *
     * @param paths 路径
     * @param def   默认值
     */
    public String getString(@NonNull Paths paths, @Nullable DefaultValue<String> def) {
        return parse(paths, bukkitSection::getString, def);
    }

    /**
     * 获取 Boolean
     *
     * @param path 路径
     */
    public boolean getBoolean(@NonNull String path) {
        return getBoolean(Paths.of(path), null);
    }

    /**
     * 获取 Boolean
     *
     * @param path 路径
     * @param def  默认值
     */
    public boolean getBoolean(@NonNull String path, @Nullable DefaultValue<Boolean> def) {
        return getBoolean(Paths.of(path), def);
    }

    /**
     * 获取 Boolean
     *
     * @param paths 路径
     * @param def   默认值
     */
    public boolean getBoolean(@NonNull Paths paths, @Nullable DefaultValue<Boolean> def) {
        return parse(paths, bukkitSection::getBoolean, def);
    }

    /**
     * 获取 Long
     *
     * @param path 路径
     */
    public long getLong(@NonNull String path) {
        return getLong(Paths.of(path), null);
    }

    /**
     * 获取 Long
     *
     * @param path 路径
     * @param def  默认值
     */
    public long getLong(@NonNull String path, @Nullable DefaultValue<Long> def) {
        return getLong(Paths.of(path), def);
    }

    /**
     * 获取 Long
     *
     * @param paths 路径
     * @param def   默认值
     */
    public long getLong(@NonNull Paths paths, @Nullable DefaultValue<Long> def) {
        return parse(paths, bukkitSection::getLong, def);
    }

    /**
     * 获取 String Set
     *
     * @param path 路径
     */
    public Set<String> getStringSet(@NonNull String path) {
        return getStringSet(Paths.of(path), null);
    }

    /**
     * 获取 String Set
     *
     * @param path 路径
     * @param def  默认值
     */
    public Set<String> getStringSet(@NonNull String path, @Nullable DefaultValue<Set<String>> def) {
        return getStringSet(Paths.of(path), def);
    }

    /**
     * 获取 String Set
     *
     * @param paths 路径
     * @param def   默认值
     */
    public Set<String> getStringSet(@NonNull Paths paths, @Nullable DefaultValue<Set<String>> def) {
        return parse(paths, path -> new HashSet<>(bukkitSection.getStringList(path)), def);
    }

    /**
     * 获取 EnumSet
     *
     * @param path  路径
     * @param clazz 枚举类
     */
    public <E extends Enum<E>> EnumSet<E> getEnumSet(@NonNull String path, @Nullable Class<E> clazz) {
        return getEnumSet(Paths.of(path), clazz, null);
    }

    /**
     * 获取 EnumSet
     *
     * @param path  路径
     * @param clazz 枚举类
     */
    public <E extends Enum<E>> EnumSet<E> getEnumSet(@NonNull String path, @Nullable Class<E> clazz, @Nullable DefaultValue<EnumSet<E>> def) {
        return getEnumSet(Paths.of(path), clazz, def);
    }

    /**
     * 获取 EnumSet
     *
     * @param paths 路径
     * @param clazz 枚举类
     * @param def   默认值
     */
    public <E extends Enum<E>> EnumSet<E> getEnumSet(@NonNull Paths paths, @NonNull Class<E> clazz, @Nullable DefaultValue<EnumSet<E>> def) {
        return getBySerializable(paths, new EnumSetSerializable<>(clazz), def);
    }

    /**
     * 获取 Enum
     *
     * @param path  路径
     * @param clazz 枚举类
     */
    public <E extends Enum<E>> E getEnum(@NonNull String path, @NonNull Class<E> clazz) {
        return getEnum(Paths.of(path), clazz, null);
    }

    /**
     * 获取 Enum
     *
     * @param path  路径
     * @param clazz 枚举类
     * @param def   默认值
     */
    public <E extends Enum<E>> E getEnum(@NonNull String path, @NonNull Class<E> clazz, @Nullable DefaultValue<E> def) {
        return getEnum(Paths.of(path), clazz, def);
    }

    /**
     * 获取 Enum
     *
     * @param paths 路径
     * @param clazz 枚举类
     * @param def   默认值
     */
    public <E extends Enum<E>> E getEnum(@NonNull Paths paths, @NonNull Class<E> clazz, @Nullable DefaultValue<E> def) {
        return getBySerializable(paths, new EnumSerializable<>(clazz), def);
    }

    /**
     * 以 Bukkit 的方式设置值
     */
    public void setByBukkit(@NonNull String path, @Nullable Object value) {
        bukkitSection.set(path, value);
    }

    /**
     * 以自定义序列化的方式获取值
     */
    public <T> T getBySerializable(@NonNull Paths paths, @Nullable Serializable<T> serializable, @Nullable DefaultValue<T> def) {
        return parse(paths, path -> serializable.get(Section.this, path), def);
    }

    /**
     * 以自定义序列化的方式设置值
     */
    public <T> void setBySerializable(@NonNull String path, @Nullable T value, @NonNull Serializable<T> serializable) {
        serializable.set(this, path, value);
    }

    /**
     * 获取配置值
     *
     * @param paths  路径
     * @param parser 解析器
     * @param def    默认值
     */
    public <T> T parse(@NonNull Paths paths, @NonNull Parser<T> parser, DefaultValue<T> def) {
        T value = null;

        for (String path : paths.getPaths()) {
            if (this.bukkitSection.contains(path)) {
                value = parser.parse(path);
                break;
            }
        }

        if (value == null) {
            if (def != null) {
                return def.getValue();
            }

            throw new RuntimeException(String.format("cannot find valid value from '%s.%s'", this.bukkitSection.getCurrentPath(), Arrays.toString(paths.getPaths())));
        }

        return value;
    }
}
