package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

expect object UiDateTimeFormatter {
    fun formatToken(
        millis: Long,
        token: DateFormatToken,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String

    fun formatDate(
        millis: Long,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String

    fun formatLocalDate(
        date: LocalDate,
        token: DateFormatToken,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String

    fun formatRelative(
        epochMillis: Long,
        nowMillis: Long = SystemClockProvider.nowMillis(),
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String
}
