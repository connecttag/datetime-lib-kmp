package org.connecttag.lib.datetime

import kotlinx.datetime.TimeZone

enum class GreetingTimePeriod {
    Morning,
    Day,
    Evening,
    Night,
}

object GreetingTimePolicy {
    private const val MORNING_RANGE = "05-11"
    private const val DAY_RANGE = "12-16"
    private const val EVENING_RANGE = "17-21"
    private const val NIGHT_RANGE = "22-04"

    fun currentPeriod(
        clockProvider: ClockProvider = SystemClockProvider,
        timeZone: TimeZone = PlatformDateTimePolicy.displayDefaultZone,
    ): GreetingTimePeriod {
        return periodForHour(
            HourRangePolicy.currentHour(
                clockProvider = clockProvider,
                timeZone = timeZone,
            ),
        )
    }

    fun periodForHour(hour: Int): GreetingTimePeriod =
        when {
            HourRangePolicy.containsHour(MORNING_RANGE, hour) -> GreetingTimePeriod.Morning
            HourRangePolicy.containsHour(DAY_RANGE, hour) -> GreetingTimePeriod.Day
            HourRangePolicy.containsHour(EVENING_RANGE, hour) -> GreetingTimePeriod.Evening
            HourRangePolicy.containsHour(NIGHT_RANGE, hour) -> GreetingTimePeriod.Night
            else -> GreetingTimePeriod.Night
        }
}
