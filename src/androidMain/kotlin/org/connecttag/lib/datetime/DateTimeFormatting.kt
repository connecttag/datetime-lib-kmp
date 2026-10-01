package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone as JavaTimeZone

internal object DateTimeFormatting {
    fun formatWithPattern(millis: Long, pattern: String, locale: Locale, zoneId: TimeZone): String {
        return SimpleDateFormat(pattern, locale).apply {
            timeZone = JavaTimeZone.getTimeZone(zoneId.id)
            isLenient = false
        }.format(Date(millis))
    }

    fun formatLocalizedDateTime(
        millis: Long,
        locale: Locale,
        zoneId: TimeZone,
        dateStyle: Int,
        timeStyle: Int,
    ): String {
        return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale)
            .formatWithZone(millis, zoneId)
    }

    fun formatLocalizedDate(
        millis: Long,
        locale: Locale,
        zoneId: TimeZone,
        dateStyle: Int,
    ): String {
        return DateFormat.getDateInstance(dateStyle, locale).formatWithZone(millis, zoneId)
    }

    fun formatLocalizedTime(
        millis: Long,
        locale: Locale,
        zoneId: TimeZone,
        timeStyle: Int,
    ): String {
        return DateFormat.getTimeInstance(timeStyle, locale).formatWithZone(millis, zoneId)
    }

    fun formatLocalDate(date: LocalDate, pattern: String, locale: Locale, zoneId: TimeZone): String {
        return formatWithPattern(date.atStartOfDayIn(zoneId).toEpochMilliseconds(), pattern, locale, zoneId)
    }

    private fun DateFormat.formatWithZone(millis: Long, zoneId: TimeZone): String {
        timeZone = JavaTimeZone.getTimeZone(zoneId.id)
        return format(Date(millis))
    }
}
