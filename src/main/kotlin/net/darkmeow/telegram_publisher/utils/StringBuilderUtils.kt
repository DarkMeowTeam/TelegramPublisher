package net.darkmeow.telegram_publisher.utils

import java.lang.StringBuilder

fun StringBuilder.toTrimString() = toString().trimEnd('\n', '\r')