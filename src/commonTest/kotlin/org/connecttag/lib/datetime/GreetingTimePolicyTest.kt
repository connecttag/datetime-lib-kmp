package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingTimePolicyTest {
    @Test
    fun periodForHourMapsConfiguredGreetingRanges() {
        assertEquals(GreetingTimePeriod.Morning, GreetingTimePolicy.periodForHour(5))
        assertEquals(GreetingTimePeriod.Morning, GreetingTimePolicy.periodForHour(11))
        assertEquals(GreetingTimePeriod.Day, GreetingTimePolicy.periodForHour(12))
        assertEquals(GreetingTimePeriod.Day, GreetingTimePolicy.periodForHour(16))
        assertEquals(GreetingTimePeriod.Evening, GreetingTimePolicy.periodForHour(17))
        assertEquals(GreetingTimePeriod.Evening, GreetingTimePolicy.periodForHour(21))
        assertEquals(GreetingTimePeriod.Night, GreetingTimePolicy.periodForHour(22))
        assertEquals(GreetingTimePeriod.Night, GreetingTimePolicy.periodForHour(4))
    }

    @Test
    fun periodForHourNormalizesWrappedHours() {
        assertEquals(GreetingTimePeriod.Night, GreetingTimePolicy.periodForHour(28))
        assertEquals(GreetingTimePeriod.Evening, GreetingTimePolicy.periodForHour(-3))
    }

    @Test
    fun currentPeriodUsesProvidedClockAndZone() {
        val instant = LocalDateTime(2026, 1, 1, 21, 30)
            .toInstant(TimeZone.UTC)
        val clock = FakeClockProvider(instant.toEpochMilliseconds())

        assertEquals(
            GreetingTimePeriod.Evening,
            GreetingTimePolicy.currentPeriod(clockProvider = clock, timeZone = TimeZone.UTC),
        )
        assertEquals(
            GreetingTimePeriod.Night,
            GreetingTimePolicy.currentPeriod(clockProvider = clock, timeZone = TimeZone.of("Asia/Riyadh")),
        )
    }
}
