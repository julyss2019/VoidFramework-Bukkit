package com.void01.bukkit.voidframework.common

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.MessageDigest


object FileUtils {
    private val DEFAULT_USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.69"

    fun downloadFromUrlOnlyResponseCodeIs200(
        urlStr: String,
        dest: File,
        overwrite: Boolean,
        userAgent: String = DEFAULT_USER_AGENT
    ) {

    }

    /**
     * 从 url 下载文件
     */
    fun downloadFromUrl(urlStr: String, dest: File, overwrite: Boolean) {
        if (dest.exists() && !overwrite) {
            return
        }

        val url = URL(urlStr)
        val rbc: ReadableByteChannel = Channels.newChannel(url.openStream())
        val fos = FileOutputStream(dest)

        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
    }

    /**
     * 计算文件 MD5
     */
    fun getMd5Sum(file: File): String {
        val md = MessageDigest.getInstance("MD5")
        val inputStream = file.inputStream()
        val buffer = ByteArray(8192)
        var bytesRead = inputStream.read(buffer)

        while (bytesRead != -1) {
            md.update(buffer, 0, bytesRead)
            bytesRead = inputStream.read(buffer)
        }

        val bytes = md.digest()
        val sb = StringBuilder()

        for (byte in bytes) {
            sb.append(String.format("%02x", byte))
        }

        return sb.toString()
    }

    /**
     * 写输入流
     */
    fun writeInputStream(input: InputStream, file: File, overwrite: Boolean) {
        if (!overwrite && file.exists()) {
            return
        }

        val parent = file.parentFile

        if (!parent.exists() && !parent.mkdirs()) {
            throw RuntimeException("Mkdirs failed: ${parent.absolutePath}")
        }

        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}