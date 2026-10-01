package org.connecttag.lib.datetime

import kotlinx.datetime.LocalTime

data class LocalTimeRange(
    val start: LocalTime,
    val endExclusive: LocalTime,
) {
    val spansMidnight: Boolean get() = start > endExclusive

    fun contains(time: LocalTime): Boolean =
        LocalTimeRangePolicy.contains(time, start, endExclusive)
}

/**
 * Product-neutral parsing and matching for local wall-clock time ranges.
 *
 * Ranges are start-inclusive and end-exclusive. A range whose bounds are equal
 * is empty; callers that mean a full day should model that explicitly.
 */
object LocalTimeRangePolicy {
    fun parseOrNull(value: String?): LocalTime? {
        val parts = value
            ?.trim()
            ?.split(":")
            ?: return null
        if (parts.size !in 2..3 || parts.any { it.isBlank() }) return null

        val hour = parts[0].toIntOrNull() ?: return null
        val minute = parts[1].toIntOrNull() ?: return null
        val second = parts.getOrNull(2)?.toIntOrNull() ?: 0

        return runCatching { LocalTime(hour, minute, second) }.getOrNull()
    }

    fun contains(
        current: LocalTime,
        start: LocalTime,
        endExclusive: LocalTime,
    ): Boolean {
        if (start == endExclusive) return false

        return if (spansMidnight(start, endExclusive)) {
            current >= start || current < endExclusive
        } else {
            current >= start && current < endExclusive
        }
    }

    fun contains(
        current: LocalTime,
        start: String?,
        endExclusive: String?,
    ): Boolean {
        val parsedStart = parseOrNull(start) ?: return false
        val parsedEnd = parseOrNull(endExclusive) ?: return false
        return contains(current, parsedStart, parsedEnd)
    }

    fun spansMidnight(start: LocalTime, endExclusive: LocalTime): Boolean = start > endExclusive

    fun parseRangeOrNull(start: String?, endExclusive: String?): LocalTimeRange? {
        val parsedStart = parseOrNull(start) ?: return null
        val parsedEnd = parseOrNull(endExclusive) ?: return null
        if (parsedStart == parsedEnd) return null
        return LocalTimeRange(parsedStart, parsedEnd)
    }
}
