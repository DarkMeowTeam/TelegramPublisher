package net.darkmeow.telegram_publisher.utils

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

inline fun <T> repo(gitDir: String = ".git", block: (Repository) -> T): T {
    val repository = FileRepositoryBuilder()
        .setGitDir(File(gitDir))
        .readEnvironment()
        .findGitDir()
        .build()

    return repository.use { block(it) }
}

inline fun <T> revWalk(repository: Repository, block: (RevWalk) -> T): T {
    val revWalk = RevWalk(repository)
    return revWalk.use { block(it) }
}