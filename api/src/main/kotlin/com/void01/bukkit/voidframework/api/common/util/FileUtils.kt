package com.void01.bukkit.voidframework.api.common.util

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.security.MessageDigest


object FileUtils {
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

        input.use {
            file.parentFile.let {
                if (!it.exists() && !it.mkdirs()) {
                    throw RuntimeException("mkdirs failed: ${it.absolutePath}")
                }
            }

            val out = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read: Int

            out.use {
                while (input.read(buffer).also { read = it } != -1) {
                    out.write(buffer, 0, read)
                }
            }
        }
    }
}