package com.github.julyss2019.bukkit.voidframework.dependency

class Dependency(val groupId: String, val artifactId: String, val version: String) {
    companion object {
        fun create(groupId: String, artifactId: String, version: String): Dependency {
            return Dependency(groupId, artifactId, version)
        }

        @JvmStatic
        fun createByGradleCoordinates(coordinates: String): Dependency {
            val split = coordinates.split(":")

            require(split.size == 3) { "invalid coordinates" }

            return Dependency(split[0], split[1], split[2])
        }
    }

    override fun toString(): String {
        return "Dependency(groupId='$groupId', artifactId='$artifactId', version='$version')"
    }
}
