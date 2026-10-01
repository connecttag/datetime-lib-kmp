package org.connecttag.lib.datetime

internal actual fun systemNowMillis(): Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
