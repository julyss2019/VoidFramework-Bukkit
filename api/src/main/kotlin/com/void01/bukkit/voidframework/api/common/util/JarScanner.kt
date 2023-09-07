package com.void01.bukkit.voidframework.api.common.util

import java.io.File
import java.io.InputStream
import java.nio.file.FileSystems
import java.nio.file.PathMatcher
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.Path


object JarScanner {
    interface ResultHandler {
        fun handle(path: String, inputStream: InputStream)
    }

    private fun parse(path: String): PathMatcher {
        return path
            .run { adjustPath(this) }
            .run {
                FileSystems.getDefault().getPathMatcher("glob:$this")
            }
    }

    private fun adjustPath(path: String): String {
        return path.run {
            if (startsWith("/")) {
                substring(1)
            } else {
                this
            }
        }.run {
            if (endsWith("/")) {
                substring(0, length - 1)
            } else {
                this
            }
        }
    }

    /**
     * 扫描
     */
    fun scan(javaClass: Class<*>, includePatterns: List<String>, excludePatterns: List<String>, resultHandler: ResultHandler) {
        val actualFile = File(javaClass.getProtectionDomain().codeSource.location.toURI())
        val includePathMatchers: List<PathMatcher> = includePatterns.map { parse(it) }
        val excludePathMatchers: List<PathMatcher> = excludePatterns.map { parse(it) }

        // 这样才能转换为正确的路径, plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() 无法正确解析非英文的路径
        JarFile(actualFile).use { jarFile ->
            val entries: Enumeration<JarEntry> = jarFile.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryPathStr = entry.name

                // 跳过文件夹
                if (entryPathStr.endsWith("/")) {
                    continue
                }

                val entryPath = Path(entryPathStr)

                if (excludePathMatchers.any { it.matches(entryPath) }) {
                    continue
                }

                if (includePathMatchers.any { it.matches(entryPath) }) {
                    resultHandler.handle(entryPathStr, jarFile.getInputStream(entry))
                }
            }
        }
    }
}