package com.void01.bukkit.voidframework.common.locale

import com.void01.bukkit.voidframework.common.yaml.Yaml2
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.*

class YamlLocaleResource2 private constructor(override val locale: Locale, val file: File) : LocaleResource2 {
    interface TextProcessor {
        fun process(text: String): String
    }

    companion object {
        /**
         * 从插件文件夹 locales 文件夹获取本土化文件
         */
        fun from(locale: Locale, plugin: Plugin): YamlLocaleResource2 {
            return YamlLocaleResource2(locale, getFile(locale, plugin.dataFolder.resolve("locales")))
        }

        private fun getFile(locale: Locale, dir: File): File {
            return dir.resolve(getFileName(locale))
        }

        private fun getFileName(locale: Locale): String {
            return locale.language + "_" + locale.country.uppercase(Locale.getDefault()) + ".yml"
        }
    }

    private lateinit var yaml: Yaml2
    var textProcessor: TextProcessor? = null

    init {
        load()
    }

    private fun load() {
        if (!file.exists()) {
            throw RuntimeException("Unable to find LocalResource file: " + file.absolutePath)
        }

        this.yaml = Yaml2.from(file)
    }

    fun reload() {
        load()
    }

    override fun getLocaleResource(path: String): LocaleResource2 {
        if (!yaml.contains(path)) {
            throw IllegalArgumentException("Unable to find LocalResource: $path")
        }

        val parentPath = path

        return object : LocaleResource2 {
            override val locale: Locale get() = this@YamlLocaleResource2.locale

            override fun getString(path: String): String {
                return this@YamlLocaleResource2.getString("${parentPath}.${path}")
            }

            override fun getStringList(path: String): List<String> {
                return this@YamlLocaleResource2.getStringList("${parentPath}.${path}")
            }

            override fun getLocaleResource(path: String): LocaleResource2 {
                return this@YamlLocaleResource2.getLocaleResource("${parentPath}.${path}")
            }
        }
    }

    override fun getStringList(path: String): List<String> {
        return yaml.getStringList(path).map { processText(it) }
    }

    override fun getString(path: String): String {
        return processText(yaml.getString(path))
    }

    private fun processText(text: String): String {
        return textProcessor?.process(text) ?: text
    }
}
