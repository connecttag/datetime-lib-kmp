package org.connecttag.lib.datetime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClockProviderTest {

    @Test
    fun fakeClockCanAdvanceDeterministically() {
        val clock = FakeClockProvider(initialMillis = 1_000L)

        clock.advanceBy(250L)

        assertEquals(1_250L, clock.nowMillis())
    }

    @Test
    fun systemClockReturnsEpochMillis() {
        assertTrue(SystemClockProvider.nowMillis() > 0L)
    }
}
