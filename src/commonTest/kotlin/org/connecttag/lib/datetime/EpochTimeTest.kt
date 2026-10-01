package org.connecttag.lib.datetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EpochTimeTest {
    @Test
    fun convertsEpochSecondsToMilliseconds() {
        assertEquals(1_800_000_000_000L, EpochTime.normalizeMillis(1_800_000_000L))
    }

    @Test
    fun preservesEpochMilliseconds() {
        assertEquals(1_800_000_000_000L, EpochTime.normalizeMillis(1_800_000_000_000L))
    }

    @Test
    fun rejectsMissingAndNonPositiveValues() {
        assertNull(EpochTime.normalizeMillis(null))
        assertNull(EpochTime.normalizeMillis(0L))
        assertNull(EpochTime.normalizeMillis(-1L))
    }
}
