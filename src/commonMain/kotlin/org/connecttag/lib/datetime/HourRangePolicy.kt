package org.connecttag.lib.datetime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toLocalDateTime

object HourRangePolicy {
    fun currentHour(
        clockProvider: ClockProvider = SystemClockProvider,
        timeZone: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): Int {
        return kotlin.time.Instant
            .fromEpochMilliseconds(clockProvider.nowMillis())
            .toLocalDateTime(timeZone)
            .hour
    }

    fun containsHour(pattern: String?, hour: Int): Boolean {
        val (start, end) = parseRange(pattern) ?: return false
        val normalizedHour = hour.floorMod(24)
        val endExclusive = (end + 1).floorMod(24)
        if (endExclusive == start) return true
        return LocalTimeRangePolicy.contains(
            current = LocalTime(normalizedHour, 0),
            start = LocalTime(start, 0),
            endExclusive = LocalTime(endExclusive, 0),
        )
    }

    private fun parseRange(pattern: String?): Pair<Int, Int>? {
        val range = pattern?.trim()?.split("-", limit = 2) ?: return null
        val start = range.getOrNull(0)?.trim()?.toIntOrNull()?.takeIf { it in 0..23 } ?: return null
        val end = range.getOrNull(1)?.trim()?.toIntOrNull()?.takeIf { it in 0..23 } ?: return null
        return start to end
    }

    private fun Int.floorMod(modulus: Int): Int = ((this % modulus) + modulus) % modulus
}
