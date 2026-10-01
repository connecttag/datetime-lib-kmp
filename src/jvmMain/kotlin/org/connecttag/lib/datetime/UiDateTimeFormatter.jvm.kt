package org.connecttag.lib.datetime

import org.connecttag.lib.text.JvmDateStyle
import org.connecttag.lib.text.formatJvmDateWithDefaultLocale
import org.connecttag.lib.text.formatJvmPatternWithDefaultLocale
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import java.util.concurrent.TimeUnit

actual object UiDateTimeFormatter {
    actual fun formatToken(
        millis: Long,
        token: DateFormatToken,
        zoneId: TimeZone,
    ): String = formatWithPattern(millis, token.pattern, zoneId)

    actual fun formatDate(
        millis: Long,
        zoneId: TimeZone,
    ): String = formatJvmDateWithDefaultLocale(
        epochMillis = millis,
        style = JvmDateStyle.SHORT,
        timeZoneId = zoneId.id,
    )

    actual fun formatLocalDate(
        date: LocalDate,
        token: DateFormatToken,
        zoneId: TimeZone,
    ): String = formatWithPattern(date.atStartOfDayIn(zoneId).toEpochMilliseconds(), token.pattern, zoneId)

    actual fun formatRelative(
        epochMillis: Long,
        nowMillis: Long,
        zoneId: TimeZone,
    ): String {
        val diff = nowMillis - epochMillis
        val thresholds = PlatformDateTimePolicy.relativeThresholds
        if (diff < 0L || diff < thresholds.justNowMaxMillis) {
            return "Now"
        }

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            diff < thresholds.hourMaxMillis -> "$minutes minutes ago"
            diff < thresholds.dayMaxMillis -> "$hours hours ago"
            days == 1L -> "Yesterday"
            diff < thresholds.relativeMaxMillis -> "$days days ago"
            else -> formatJvmDateWithDefaultLocale(
                epochMillis = epochMillis,
                style = JvmDateStyle.MEDIUM,
                timeZoneId = zoneId.id,
            )
        }
    }

    private fun formatWithPattern(millis: Long, pattern: String, zoneId: TimeZone): String {
        return formatJvmPatternWithDefaultLocale(
            epochMillis = millis,
            pattern = pattern,
            timeZoneId = zoneId.id,
        )
    }
}
