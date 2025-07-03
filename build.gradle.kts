import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val baseGroup: String by project
val baseVersion: String by project

val jgitVersion: String by project
val telegramBotVersion: String by project

group = baseGroup
version = baseVersion

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("telegram-publisher") {
            id = "net.darkmeow.telegram-publisher"
            displayName = "Telegram publisher"
            description = "Publish release on telegram channel."
            implementationClass = "net.darkmeow.telegram_publisher.TelegramPublisherPlugin"
        }
    }
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType(KotlinCompile::class.java) {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

publishing {
    val mavenAuth = System.getenv("MAVEN_USERNAME")?.let { username ->
        System.getenv("MAVEN_PASSWORD")?.let { password ->
            username to password
        }
    }

    repositories {
        mavenLocal()

        mavenAuth?.also { auth ->
            maven {
                url = uri("https://nekocurit.asia/repository/release/")

                credentials {
                    username = auth.first
                    password = auth.second
                }
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:$jgitVersion")
    implementation("com.github.pengrad:java-telegram-bot-api:$telegramBotVersion")
}