package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

actual object UiDateTimeFormatter {
    actual fun formatToken(millis: Long, token: DateFormatToken, zoneId: TimeZone): String =
        PortableUiDateTimeFormatter.formatToken(millis, token, zoneId)

    actual fun formatDate(millis: Long, zoneId: TimeZone): String =
        PortableUiDateTimeFormatter.formatDate(millis, zoneId)

    actual fun formatLocalDate(date: LocalDate, token: DateFormatToken, zoneId: TimeZone): String =
        PortableUiDateTimeFormatter.formatLocalDate(date, token, zoneId)

    actual fun formatRelative(epochMillis: Long, nowMillis: Long, zoneId: TimeZone): String =
        PortableUiDateTimeFormatter.formatRelative(epochMillis, nowMillis, zoneId)
}
