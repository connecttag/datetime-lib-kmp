package org.connecttag.lib.datetime

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalTimeRangePolicyTest {
    @Test
    fun parseOrNullAcceptsSupportedTimeShapes() {
        assertEquals(LocalTime(9, 5), LocalTimeRangePolicy.parseOrNull(" 09:05 "))
        assertEquals(LocalTime(9, 5, 7), LocalTimeRangePolicy.parseOrNull("09:05:07"))
    }

    @Test
    fun parseOrNullRejectsInvalidOrAmbiguousValues() {
        assertNull(LocalTimeRangePolicy.parseOrNull(null))
        assertNull(LocalTimeRangePolicy.parseOrNull(""))
        assertNull(LocalTimeRangePolicy.parseOrNull("09"))
        assertNull(LocalTimeRangePolicy.parseOrNull("24:00"))
        assertNull(LocalTimeRangePolicy.parseOrNull("09:05:07:10"))
    }

    @Test
    fun containsUsesStartInclusiveAndEndExclusiveBounds() {
        val start = LocalTime(9, 0)
        val end = LocalTime(17, 0)

        assertTrue(LocalTimeRangePolicy.contains(LocalTime(9, 0), start, end))
        assertTrue(LocalTimeRangePolicy.contains(LocalTime(16, 59, 59), start, end))
        assertFalse(LocalTimeRangePolicy.contains(LocalTime(17, 0), start, end))
    }

    @Test
    fun containsMatchesRangesThatSpanMidnight() {
        val start = LocalTime(22, 0)
        val end = LocalTime(4, 0)

        assertTrue(LocalTimeRangePolicy.contains(LocalTime(23, 30), start, end))
        assertTrue(LocalTimeRangePolicy.contains(LocalTime(2, 0), start, end))
        assertFalse(LocalTimeRangePolicy.contains(LocalTime(4, 0), start, end))
        assertFalse(LocalTimeRangePolicy.contains(LocalTime(12, 0), start, end))
    }

    @Test
    fun containsTreatsEqualBoundsAsAnEmptyRange() {
        assertFalse(
            LocalTimeRangePolicy.contains(
                current = LocalTime(12, 0),
                start = LocalTime(9, 0),
                endExclusive = LocalTime(9, 0),
            ),
        )
    }
}
