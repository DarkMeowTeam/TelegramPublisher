package net.darkmeow.telegram_publisher

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendDocument
import net.darkmeow.telegram_publisher.utils.*
import org.gradle.api.Plugin
import org.gradle.api.Project


@Suppress("unused")
class TelegramPublisherPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("telegramPublisher", TelegramPublisherExtension::class.java)

        project.tasks.register("telegramPublisher") {
            it.doLast {
                val file = extension.artifact.get().asFile

                TelegramBot(extension.token.get())
                    .apply {
                        val message = generateCommitMessage(project)

                        execute(
                            SendDocument(extension.chatId.get(), file)
                                .caption(message.second)
                                .fileName("${message.first}.jar")

                        )
                    }
            }
        }
    }

    fun generateCommitMessage(project: Project) = repo(gitDir = ".git") { repo ->
        revWalk(repository = repo) { revWalk ->
            val headCommit = revWalk.parseCommit(repo.resolve("HEAD"))
            val prevCommit = project.getPrevCommit() ?: headCommit.parents.firstOrNull()?.name ?: "no_prev"

            project.setPrevCommit(headCommit.name)
            revWalk.markStart(revWalk.parseCommit(headCommit))

            return@revWalk Pair(
                first = headCommit.name,
                second =  revWalk
                    .takeWhile { !prevCommit.contains(it.name) }
                    .joinToString("\n\n") { commit ->
                        StringBuilder()
                            .apply {
                                // # 便于方便 Telegram 中统计贡献者贡献数/筛选指定贡献者的 commit
                                appendLine("${commit.name}  by #${commit.authorIdent.name}")
                                appendLine(commit.fullMessage.trim().replace("\n\n", "\n"))
                            }
                            .toTrimString()
                    }
            )
        }
    }
}