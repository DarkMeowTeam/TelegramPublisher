package net.darkmeow.telegram_publisher

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class TelegramPublisherExtension {

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<Long>

    @get:Input
    abstract val artifact: RegularFileProperty

}