package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object PlatformDateTimeMappers {

    fun parseIsoInstant(value: String?): kotlin.time.Instant? {
        val trimmed = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return runCatching { kotlin.time.Instant.parse(trimmed) }.getOrNull()
    }

    fun parseEpochMillis(
        value: String?,
        normalizeSecondsEpoch: Boolean = true,
    ): Long? {
        val raw = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return raw.toLongOrNull()?.let { parsed ->
            if (normalizeSecondsEpoch && raw.length <= 10) parsed * 1000L else parsed
        } ?: parseIsoInstant(raw)?.toEpochMilliseconds()
    }

    fun parseFlexibleEpochMillis(
        value: String?,
        zoneId: TimeZone = PlatformDateTimePolicy.serverZone,
        normalizeSecondsEpoch: Boolean = true,
    ): Long? {
        val raw = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        parseEpochMillis(raw, normalizeSecondsEpoch)?.let { return it }

        val normalized = raw
            .removeSuffix("Z")
            .replace(' ', 'T')
            .replace('/', '-')

        runCatching { LocalDateTime.parse(normalized).toInstant(zoneId).toEpochMilliseconds() }
            .getOrNull()
            ?.let { return it }

        runCatching { LocalDate.parse(normalized).atStartOfDayIn(zoneId).toEpochMilliseconds() }
            .getOrNull()
            ?.let { return it }

        parseSlashDate(raw, zoneId)?.let { return it }
        return null
    }

    fun parseLocalDateTime(value: String?): LocalDateTime? {
        val trimmed = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return runCatching { LocalDateTime.parse(trimmed) }.getOrNull()
    }

    fun parseLocalDate(value: String?): LocalDate? {
        val trimmed = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return runCatching { LocalDate.parse(trimmed) }.getOrNull()
    }

    fun parseLocalTime(value: String?): LocalTime? {
        val trimmed = value?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return runCatching { LocalTime.parse(trimmed) }.getOrNull()
    }

    fun localDateToStartOfDayMillis(
        date: LocalDate,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): Long {
        return date.atStartOfDayIn(zoneId).toEpochMilliseconds()
    }

    fun epochMillisToInstant(value: Long?): kotlin.time.Instant? {
        return value?.let(kotlin.time.Instant::fromEpochMilliseconds)
    }

    fun epochMillisToLocalDate(
        value: Long?,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): LocalDate? {
        return value
            ?.let(kotlin.time.Instant::fromEpochMilliseconds)
            ?.toLocalDateTime(zoneId)
            ?.date
    }

    fun instantToEpochMillis(value: kotlin.time.Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    private fun parseSlashDate(value: String, zoneId: TimeZone): Long? {
        val datePart = value.substringBefore(' ').substringBefore('T')
        val parts = datePart.split('/', '-')
        if (parts.size != 3) return null
        val first = parts[0].toIntOrNull() ?: return null
        val second = parts[1].toIntOrNull() ?: return null
        val third = parts[2].toIntOrNull() ?: return null
        val date = if (parts[0].length == 4) {
            LocalDate(first, second, third)
        } else {
            LocalDate(third, second, first)
        }
        return date.atStartOfDayIn(zoneId).toEpochMilliseconds()
    }
}

object DateTimeMappers {
    fun parseIsoInstant(value: String?): kotlin.time.Instant? {
        return PlatformDateTimeMappers.parseIsoInstant(value)
    }

    fun parseEpochMillis(
        value: String?,
        normalizeSecondsEpoch: Boolean = true,
    ): Long? {
        return PlatformDateTimeMappers.parseEpochMillis(value, normalizeSecondsEpoch)
    }

    fun parseFlexibleEpochMillis(
        value: String?,
        zoneId: TimeZone = DateTimePolicy.serverZone,
        normalizeSecondsEpoch: Boolean = true,
    ): Long? {
        return PlatformDateTimeMappers.parseFlexibleEpochMillis(
            value = value,
            zoneId = zoneId,
            normalizeSecondsEpoch = normalizeSecondsEpoch,
        )
    }

    fun parseApiInstant(
        value: String?,
        zoneId: TimeZone = DateTimePolicy.serverZone,
        locale: DateTimeLocale = DateTimePolicy.parseLocale,
        patterns: List<String> = DateTimePolicy.fallbackPatterns,
    ): kotlin.time.Instant? {
        return parseApiEpochMillis(
            value = value,
            zoneId = zoneId,
            locale = locale,
            patterns = patterns,
        )?.let(kotlin.time.Instant::fromEpochMilliseconds)
    }

    fun parseApiEpochMillis(
        value: String?,
        zoneId: TimeZone = DateTimePolicy.serverZone,
        locale: DateTimeLocale = DateTimePolicy.parseLocale,
        normalizeSecondsEpoch: Boolean = true,
        patterns: List<String> = DateTimePolicy.fallbackPatterns,
    ): Long? {
        return PlatformDateTimeMappers.parseFlexibleEpochMillis(
            value = value,
            zoneId = zoneId,
            normalizeSecondsEpoch = normalizeSecondsEpoch,
        )
    }

    fun parseUiLocalDate(
        value: String?,
        locale: DateTimeLocale = DateTimePolicy.parseLocale,
        patterns: List<String> = DateTimePolicy.uiDateInputPatterns,
    ): LocalDate? {
        return PlatformDateTimeMappers.parseLocalDate(value)
            ?: PlatformDateTimeMappers
                .parseFlexibleEpochMillis(value, TimeZone.UTC)
                ?.let { PlatformDateTimeMappers.epochMillisToLocalDate(it, TimeZone.UTC) }
    }

    fun parseUiLocalTime(
        value: String?,
        token: DateFormatToken = DateFormatToken.TIME_24_HOUR,
        locale: DateTimeLocale = DateTimePolicy.parseLocale,
    ): LocalTime? {
        return PlatformDateTimeMappers.parseLocalTime(value)
    }

    fun parseLocalDate(value: String?): LocalDate? {
        return PlatformDateTimeMappers.parseLocalDate(value)
    }

    fun parseUiDateTimeEpochMillis(
        value: String?,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
        locale: DateTimeLocale = DateTimePolicy.parseLocale,
        patterns: List<String> = DateTimePolicy.uiDateTimeInputPatterns,
    ): Long? {
        return PlatformDateTimeMappers.parseFlexibleEpochMillis(
            value = value,
            zoneId = zoneId,
        )
    }

    fun localDateToStartOfDayMillis(
        date: LocalDate,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): Long {
        return PlatformDateTimeMappers.localDateToStartOfDayMillis(date, zoneId)
    }

    fun epochMillisToInstant(value: Long?): kotlin.time.Instant? {
        return PlatformDateTimeMappers.epochMillisToInstant(value)
    }

    fun epochMillisToLocalDate(
        value: Long?,
        zoneId: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): LocalDate? {
        return PlatformDateTimeMappers.epochMillisToLocalDate(value, zoneId)
    }

    fun instantToEpochMillis(value: kotlin.time.Instant?): Long? {
        return PlatformDateTimeMappers.instantToEpochMillis(value)
    }
}

fun String?.toEpochMillisOrNull(): Long? = DateTimeMappers.parseApiEpochMillis(this)
