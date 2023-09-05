package com.github.julyss2019.bukkit.voidframework.dependency

class Repository(url: String) {
    private var url: String = url
        set(value) {
            if (value.endsWith("/")) {
                field = value.substring(0, value.length - 1)
            }

            field = value
        }

    fun getJarUrl(dependency: Dependency): String {
        return getUrlBySuffix(dependency, ".jar")
    }

    fun getJarSha1Url(dependency: Dependency): String {
        return getUrlBySuffix(dependency, ".jar.sha1")
    }

    fun getPomUrl(dependency: Dependency): String {
        return getUrlBySuffix(dependency, ".pom")
    }

    fun getPomSha1Url(dependency: Dependency): String {
        return getUrlBySuffix(dependency, ".pom.sha1")
    }

    private fun getUrlBySuffix(dependency: Dependency, suffix: String): String {
        return "$url/${dependency.groupId.replace(".", "/")}/${dependency.artifactId}/${dependency.version}/${dependency.artifactId}-${dependency.version}$suffix"
    }

    override fun toString(): String {
        return "Repository(url='$url')"
    }
}
