package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.DateFormat
import java.util.Locale

object DateTimeUtil {
    fun nowMillis(): Long = SystemClockProvider.nowMillis()

    fun String?.toEpochMillisOrNull(
        zoneId: TimeZone = TimeZone.UTC,
        locale: Locale = Locale.US,
        normalizeSecondsEpoch: Boolean = false,
    ): Long? = DateTimeParsing.toEpochMillisOrNull(this, zoneId, locale, normalizeSecondsEpoch)

    fun nowLocalDateTime(nowMillis: Long): LocalDateTime =
        kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(DateTimePolicy.serverZone)

    fun parseServerDate(value: String?): kotlin.time.Instant? =
        parseDateLikeString(value, DateTimePolicy.serverZone, Locale.US, DateTimePolicy.fallbackPatterns)

    fun parseDateLikeString(
        value: String?,
        zoneId: TimeZone = DateTimePolicy.serverZone,
        locale: Locale = Locale.US,
        patterns: List<String> = DateTimePolicy.fallbackPatterns,
    ): kotlin.time.Instant? = DateTimeParsing.parseDateLikeString(value, zoneId, locale, patterns)

    fun parseDateLikeToEpochMillis(
        value: String?,
        zoneId: TimeZone = DateTimePolicy.serverZone,
        locale: Locale = Locale.US,
        patterns: List<String> = DateTimePolicy.fallbackPatterns,
    ): Long? = DateTimeParsing.parseDateLikeToEpochMillis(value, zoneId, locale, patterns)

    fun parseDateOnly(
        value: String?,
        locale: Locale = Locale.US,
        patterns: List<String> = DateTimePolicy.uiDateInputPatterns,
    ): LocalDate? = DateTimeParsing.parseDateOnly(value, locale, patterns)

    fun parseTimeOfDay(
        value: String?,
        pattern: String = DateFormatToken.TIME_24_HOUR.pattern,
        locale: Locale = Locale.US,
    ): LocalTime? = DateTimeParsing.parseTimeOfDay(value, pattern, locale)

    fun parseRfc1123ToEpochMillis(value: String): Long? =
        DateTimeParsing.parseRfc1123ToEpochMillis(value)

    fun formatWithPattern(
        millis: Long,
        pattern: String,
        locale: Locale = Locale.US,
        zoneId: TimeZone = DateTimePolicy.serverZone,
    ): String = DateTimeFormatting.formatWithPattern(millis, pattern, locale, zoneId)

    fun formatWithToken(
        millis: Long,
        token: DateFormatToken,
        locale: Locale = Locale.US,
        zoneId: TimeZone = DateTimePolicy.serverZone,
    ): String = formatWithPattern(millis, token.pattern, locale, zoneId)

    fun formatLocalizedDateTime(
        millis: Long,
        locale: Locale,
        zoneId: TimeZone,
        dateStyle: Int = DateFormat.SHORT,
        timeStyle: Int = DateFormat.SHORT,
    ): String = DateTimeFormatting.formatLocalizedDateTime(millis, locale, zoneId, dateStyle, timeStyle)

    fun formatLocalizedDate(millis: Long, locale: Locale, zoneId: TimeZone, dateStyle: Int = DateFormat.SHORT): String =
        DateTimeFormatting.formatLocalizedDate(millis, locale, zoneId, dateStyle)

    fun formatLocalizedTime(millis: Long, locale: Locale, zoneId: TimeZone, timeStyle: Int = DateFormat.SHORT): String =
        DateTimeFormatting.formatLocalizedTime(millis, locale, zoneId, timeStyle)

    fun formatLocalDate(
        date: LocalDate,
        pattern: String,
        locale: Locale = Locale.getDefault(),
        zoneId: TimeZone = DateTimePolicy.displayDefaultZone,
    ): String = DateTimeFormatting.formatLocalDate(date, pattern, locale, zoneId)

    fun isExpired(expiryMillis: Long?, nowMillis: Long): Boolean =
        DateTimeComparisons.isExpiredAt(expiryMillis, nowMillis)
}
