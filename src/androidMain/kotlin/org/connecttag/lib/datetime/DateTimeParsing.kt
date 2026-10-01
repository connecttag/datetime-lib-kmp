package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone as JavaTimeZone

internal object DateTimeParsing {
    fun toEpochMillisOrNull(
        value: String?,
        zoneId: TimeZone,
        locale: Locale,
        normalizeSecondsEpoch: Boolean,
    ): Long? {
        val raw = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        raw.toLongOrNull()?.let { return if (normalizeSecondsEpoch && raw.length <= 10) it * 1000L else it }
        return parseDateLikeToEpochMillis(raw, zoneId, locale, PlatformDateTimePolicy.fallbackPatterns)
    }

    fun parseDateLikeString(
        value: String?,
        zoneId: TimeZone,
        locale: Locale,
        patterns: List<String>,
    ): kotlin.time.Instant? {
        val trimmed = value?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        runCatching { return kotlin.time.Instant.parse(trimmed) }
        patterns.forEach { pattern ->
            if (pattern.contains('X')) return@forEach
            val parseZone = if (pattern.contains("'Z'")) TimeZone.UTC else zoneId
            parseExact(trimmed, pattern, locale, parseZone)?.let {
                return kotlin.time.Instant.fromEpochMilliseconds(it.time)
            }
        }
        return null
    }

    fun parseDateLikeToEpochMillis(
        value: String?,
        zoneId: TimeZone,
        locale: Locale,
        patterns: List<String>,
    ): Long? = parseDateLikeString(value, zoneId, locale, patterns)?.toEpochMilliseconds()

    fun parseDateOnly(value: String?, locale: Locale, patterns: List<String>): LocalDate? {
        val trimmed = value?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        patterns.forEach { pattern ->
            parseExact(trimmed, pattern, locale, TimeZone.UTC)?.let { parsed ->
                val calendar = Calendar.getInstance(JavaTimeZone.getTimeZone("UTC"), locale)
                calendar.time = parsed
                return LocalDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                )
            }
        }
        return null
    }

    fun parseTimeOfDay(value: String?, pattern: String, locale: Locale): LocalTime? {
        val parsed = parseExact(value?.trim(), pattern, locale, TimeZone.UTC) ?: return null
        val calendar = Calendar.getInstance(JavaTimeZone.getTimeZone("UTC"), locale)
        calendar.time = parsed
        return LocalTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
    }

    fun parseRfc1123ToEpochMillis(value: String): Long? {
        return parseExact(value, "EEE, dd MMM yyyy HH:mm:ss Z", Locale.US, TimeZone.UTC)?.time
            ?: parseExact(value, "EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US, TimeZone.UTC)?.time
    }

    private fun parseExact(value: String?, pattern: String, locale: Locale, zoneId: TimeZone): Date? {
        val raw = value?.takeIf { it.isNotEmpty() } ?: return null
        val format = SimpleDateFormat(pattern, locale).apply {
            timeZone = JavaTimeZone.getTimeZone(zoneId.id)
            isLenient = false
        }
        val position = ParsePosition(0)
        val parsed = format.parse(raw, position) ?: return null
        return parsed.takeIf { position.index == raw.length }
    }
}
