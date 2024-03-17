package com.void01.bukkit.voidframework.common

import java.io.File
import java.io.InputStream
import java.nio.file.FileSystems
import java.nio.file.PathMatcher
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.Path

class JarScanner2(val clazz: Class<*>, val includePatterns: List<String>, val excludePatterns: List<String> = emptyList()) {
    interface ScanResultHandler {
        fun handle(path: String, inputStream: InputStream)
    }

    companion object {
        private fun adjustPath(path: String): String {
            return path
                .run {
                    if (startsWith("/")) {
                        substring(1)
                    } else {
                        this
                    }
                }
                .run {
                    if (endsWith("/")) {
                        substring(0, length - 1)
                    } else {
                        this
                    }
                }
        }

        private fun parse(path: String): PathMatcher {
            return path
                .run { adjustPath(this) }
                .run {
                    FileSystems.getDefault().getPathMatcher("glob:$this")
                }
        }
    }

    fun scan(resultHandler: ScanResultHandler) {
        val actualFile = File(clazz.getProtectionDomain().codeSource.location.toURI())
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