package net.darkmeow.telegram_publisher.utils

import org.gradle.api.Project
import java.io.File

fun Project.getPluginDir(): File = file(".gradle/net.darkmeow.telegram-publisher")
    .apply {
        mkdirs()
    }

fun Project.getPrevCommit(): String? {
    return getPluginDir().resolve("prev_commit_hash.txt")
        .takeIf { it.exists() }
        ?.readText()
}

fun Project.setPrevCommit(hash: String) {
    getPluginDir().resolve("prev_commit_hash.txt").writeText(hash)
}