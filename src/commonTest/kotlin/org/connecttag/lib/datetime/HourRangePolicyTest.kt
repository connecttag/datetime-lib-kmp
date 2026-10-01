package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HourRangePolicyTest {
    @Test
    fun containsHourMatchesSameDayRange() {
        assertTrue(HourRangePolicy.containsHour("05-11", 5))
        assertTrue(HourRangePolicy.containsHour("05-11", 11))
        assertFalse(HourRangePolicy.containsHour("05-11", 12))
    }

    @Test
    fun containsHourMatchesWrappedRange() {
        assertTrue(HourRangePolicy.containsHour("22-04", 22))
        assertTrue(HourRangePolicy.containsHour("22-04", 2))
        assertTrue(HourRangePolicy.containsHour("22-04", 28))
        assertFalse(HourRangePolicy.containsHour("22-04", 12))
    }

    @Test
    fun containsHourRejectsInvalidPatterns() {
        assertFalse(HourRangePolicy.containsHour(null, 8))
        assertFalse(HourRangePolicy.containsHour("bad", 8))
        assertFalse(HourRangePolicy.containsHour("24-25", 8))
    }

    @Test
    fun currentHourUsesProvidedClockAndZone() {
        val instant = LocalDateTime(2026, 1, 1, 21, 30)
            .toInstant(TimeZone.UTC)
        val clock = FakeClockProvider(instant.toEpochMilliseconds())

        assertEquals(21, HourRangePolicy.currentHour(clockProvider = clock, timeZone = TimeZone.UTC))
        assertEquals(0, HourRangePolicy.currentHour(clockProvider = clock, timeZone = TimeZone.of("Asia/Riyadh")))
    }
}
