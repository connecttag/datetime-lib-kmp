package org.connecttag.lib.datetime

import android.content.Context
import androidx.core.os.ConfigurationCompat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.text.DateFormat
import java.util.Locale
import android.text.format.DateFormat as AndroidDateFormat
import java.util.TimeZone as JavaTimeZone

actual object UiDateTimeFormatter {
    actual fun formatToken(
        millis: Long,
        token: DateFormatToken,
        zoneId: TimeZone,
    ): String = DateTimeFormatting.formatWithPattern(millis, token.pattern, Locale.getDefault(), zoneId)

    fun formatToken(
        millis: Long,
        token: DateFormatToken,
        locale: Locale,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String = DateTimeFormatting.formatWithPattern(millis, token.pattern, locale, zoneId)

    fun formatDateTime(
        millis: Long,
        dateStyle: Int = DateFormat.SHORT,
        timeStyle: Int = DateFormat.SHORT,
        locale: Locale = Locale.getDefault(),
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String {
        return DateTimeFormatting.formatLocalizedDateTime(millis, locale, zoneId, dateStyle, timeStyle)
    }

    actual fun formatDate(
        millis: Long,
        zoneId: TimeZone,
    ): String = DateTimeFormatting.formatLocalizedDate(millis, Locale.getDefault(), zoneId, DateFormat.SHORT)

    fun formatDate(
        millis: Long,
        dateStyle: Int = DateFormat.SHORT,
        locale: Locale = Locale.getDefault(),
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String = DateTimeFormatting.formatLocalizedDate(millis, locale, zoneId, dateStyle)

    fun formatTime(
        context: Context,
        millis: Long,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String {
        return AndroidDateFormat.getTimeFormat(context).apply {
            timeZone = JavaTimeZone.getTimeZone(zoneId.id)
        }.format(millis)
    }

    actual fun formatLocalDate(
        date: LocalDate,
        token: DateFormatToken,
        zoneId: TimeZone,
    ): String = DateTimeFormatting.formatLocalDate(date, token.pattern, Locale.getDefault(), zoneId)

    fun formatLocalDate(
        date: LocalDate,
        token: DateFormatToken,
        locale: Locale,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String = DateTimeFormatting.formatLocalDate(date, token.pattern, locale, zoneId)

    fun formatRelative(
        context: Context,
        epochMillis: Long,
        nowMillis: Long = SystemClockProvider.nowMillis(),
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String {
        val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
            ?: Locale.getDefault()
        return formatRelative(epochMillis, nowMillis, locale, zoneId)
    }

    actual fun formatRelative(
        epochMillis: Long,
        nowMillis: Long,
        zoneId: TimeZone,
    ): String = formatRelative(epochMillis, nowMillis, Locale.getDefault(), zoneId)

    fun formatRelative(
        epochMillis: Long,
        nowMillis: Long = SystemClockProvider.nowMillis(),
        locale: Locale = Locale.getDefault(),
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): String = UiRelativeDateTimeFormatter.formatRelative(epochMillis, nowMillis, locale, zoneId)
}
