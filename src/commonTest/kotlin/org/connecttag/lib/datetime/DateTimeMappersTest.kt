package org.connecttag.lib.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DateTimeMappersTest {

    @Test
    fun parsesSecondsEpochWhenRequested() {
        assertEquals(1_700_000_000_000L, PlatformDateTimeMappers.parseEpochMillis("1700000000"))
    }

    @Test
    fun parsesIsoInstantToEpochMillis() {
        assertEquals(
            1_700_000_000_000L,
            PlatformDateTimeMappers.parseEpochMillis("2023-11-14T22:13:20Z"),
        )
    }

    @Test
    fun mapsEpochMillisToLocalDateInZone() {
        assertEquals(
            LocalDate(2023, 11, 15),
            PlatformDateTimeMappers.epochMillisToLocalDate(
                value = 1_700_000_000_000L,
                zoneId = TimeZone.of("Asia/Riyadh"),
            ),
        )
    }

    @Test
    fun businessCalendarCalculatesWeekStart() {
        val policy = PlatformDateTimePolicy.businessCalendar

        assertEquals(
            LocalDate(2023, 11, 11),
            policy.weekStart(LocalDate(2023, 11, 15)),
        )
    }

    @Test
    fun comparisonsHandleNullableTargets() {
        assertFalse(DateTimeComparisons.isExpiredAt(null, nowMillis = 10L))
        assertTrue(DateTimeComparisons.isExpiredAt(10L, nowMillis = 10L))
        assertNotNull(PlatformDateTimeMappers.parseLocalDate("2023-11-15"))
    }
}
