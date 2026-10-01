package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

internal object PortableUiDateTimeFormatter {
    fun formatToken(millis: Long, token: DateFormatToken, zoneId: TimeZone): String {
        val value = kotlin.time.Instant.fromEpochMilliseconds(millis).toLocalDateTime(zoneId)
        return value.format(token)
    }

    fun formatDate(millis: Long, zoneId: TimeZone): String {
        return kotlin.time.Instant.fromEpochMilliseconds(millis).toLocalDateTime(zoneId).date.toString()
    }

    fun formatLocalDate(date: LocalDate, token: DateFormatToken, zoneId: TimeZone): String {
        return date.atStartOfDayIn(zoneId).toLocalDateTime(zoneId).format(token)
    }

    fun formatRelative(epochMillis: Long, nowMillis: Long, zoneId: TimeZone): String {
        val diff = nowMillis - epochMillis
        val thresholds = PlatformDateTimePolicy.relativeThresholds
        if (diff < thresholds.justNowMaxMillis) return "Now"

        val minutes = diff / 60_000L
        val hours = diff / 3_600_000L
        val days = diff / 86_400_000L
        return when {
            diff < thresholds.hourMaxMillis -> "$minutes minutes ago"
            diff < thresholds.dayMaxMillis -> "$hours hours ago"
            days == 1L -> "Yesterday"
            diff < thresholds.relativeMaxMillis -> "$days days ago"
            else -> formatDate(epochMillis, zoneId)
        }
    }

    private fun LocalDateTime.format(token: DateFormatToken): String {
        val yearText = year.toString().padStart(4, '0')
        val monthText = month.number.toString().padStart(2, '0')
        val dayText = day.toString().padStart(2, '0')
        val hourText = hour.toString().padStart(2, '0')
        val minuteText = minute.toString().padStart(2, '0')
        val secondText = second.toString().padStart(2, '0')
        val millisText = (nanosecond / 1_000_000).toString().padStart(3, '0')
        val monthName = MONTH_NAMES[month.number - 1]
        val twelveHour = when (val normalized = hour % 12) {
            0 -> 12
            else -> normalized
        }
        val period = if (hour < 12) "AM" else "PM"
        return when (token) {
            DateFormatToken.UI_DATE_INPUT -> "$yearText-$monthText-$dayText"
            DateFormatToken.UI_DATE_TIME_INPUT -> "$yearText-$monthText-$dayText $hourText:$minuteText"
            DateFormatToken.TIME_24_HOUR -> "$hourText:$minuteText"
            DateFormatToken.API_ISO_UTC_MILLIS -> "$yearText-$monthText-${dayText}T$hourText:$minuteText:$secondText.${millisText}Z"
            DateFormatToken.SERVER_DATE_TIME -> "$yearText-$monthText-$dayText $hourText:$minuteText:$secondText"
            DateFormatToken.CALENDAR_DAY_SLASH -> "$yearText/$monthText/$dayText"
            DateFormatToken.CALENDAR_DAY_MONTH_SLASH -> "$dayText/$monthText/$yearText"
            DateFormatToken.CALENDAR_MONTH_DAY -> "$day $monthName"
            DateFormatToken.CALENDAR_DAY_MONTH_YEAR -> "$day $monthName $yearText"
            DateFormatToken.CALENDAR_DAY_MONTH_YEAR_TIME -> "$day $monthName $yearText - $twelveHour:$minuteText $period"
        }
    }

    private val MONTH_NAMES = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December",
    )
}
