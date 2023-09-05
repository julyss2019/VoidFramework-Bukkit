package com.github.julyss2019.bukkit.voidframework.dependency

import com.github.julyss2019.bukkit.voidframework.common.Files as VoidFiles
import com.github.julyss2019.bukkit.voidframework.internal.VoidFrameworkPlugin
import java.io.File
import java.lang.RuntimeException

class DependencyLoader(val repositories: List<Repository>) {
    private enum class FileType(val suffix: String) {
        JAR(".jar"),
        JAR_SHA1(".jar.sha1"),
        POM(".pom"),
        POM_SHA1(".pom.sha1")
    }

    companion object {
        private fun readSha1(file: File): String {
            val lines = file.readLines()

            if (lines.isEmpty()) {
                throw RuntimeException("invalid SHA-1 file: ${file.absolutePath}")
            }

            return lines[0]
        }
    }

    fun load(dependency: Dependency, relocations: List<Relocation>, resolveSubDependencies: Boolean) {
        if (!isJarAvailable(dependency)) {
            downloadJar(dependency)
        }

        if (!resolveSubDependencies) {
            return
        }

        if (!isPomAvailable(dependency)) {
            downloadPom(dependency)
        }
    }

    private fun resolvePom(dependency: Dependency) {

    }

    private fun downloadPom(dependency: Dependency) {
        for (repository in repositories) {
            val pomSha1Url = repository.getPomSha1Url(dependency)
            val pomUrl = repository.getPomUrl(dependency)

            downloadAndVerify(pomUrl, getDependencyFile(dependency, FileType.JAR), pomSha1Url, getDependencyFile(dependency, FileType.JAR_SHA1))
            return
        }
    }

    private fun relocateJar(dependency: Dependency) {

    }

    private fun downloadJar(dependency: Dependency) {
        for (repository in repositories) {
            val jarSha1Url = repository.getJarSha1Url(dependency)
            val jarUrl = repository.getJarUrl(dependency)

            downloadAndVerify(jarUrl, getDependencyFile(dependency, FileType.JAR), jarSha1Url, getDependencyFile(dependency, FileType.JAR_SHA1))
            return
        }
    }

    private fun downloadAndVerify(url: String, dest: File, sha1Url: String, sha1Dest: File) {
        println("Downloading: $sha1Url")
        VoidFiles.download(sha1Url, sha1Dest)

        println("Downloading: $url")
        VoidFiles.download(url, dest)

        val actual = VoidFiles.getSha1(dest)
        val excepted = readSha1(sha1Dest)

        if (actual != excepted) {
            throw RuntimeException("SHA-1 not match, excepted: $excepted, actual: $actual")
        }
    }

    /**
     * POM 文件是否可用
     */
    private fun isPomAvailable(dependency: Dependency): Boolean {
        val pomSha1File = getDependencyFile(dependency, FileType.POM_SHA1)
        val pomFile = getDependencyFile(dependency, FileType.POM)

        return pomSha1File.exists() && pomFile.exists() && VoidFiles.getSha1(pomFile) == readSha1(pomSha1File)
    }

    /**
     * JAR 文件是否可用
     */
    private fun isJarAvailable(dependency: Dependency): Boolean {
        val jarSha1File = getDependencyFile(dependency, FileType.JAR_SHA1)
        val jarFile = getDependencyFile(dependency, FileType.JAR)

        return jarSha1File.exists() && jarFile.exists() && VoidFiles.getSha1(jarFile) == readSha1(jarSha1File)
    }

    private fun getDependencyFile(dependency: Dependency, fileType: FileType): File {
        return File(getDependencyDir(dependency), dependency.artifactId + "-" + dependency.version + fileType.suffix)
    }

    private fun getDependencyDir(dependency: Dependency): File {
        return File(
            VoidFrameworkPlugin.getInst().librariesDir, dependency.groupId.replace(".", "/") + File.separator
                    + dependency.artifactId + File.separator
                    + dependency.version
        )
    }
}
