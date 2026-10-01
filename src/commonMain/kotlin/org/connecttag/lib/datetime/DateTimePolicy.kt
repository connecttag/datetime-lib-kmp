package org.connecttag.lib.datetime

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

data class DateTimeLocale(
    val tag: String,
) {
    val language: String = tag
        .substringBefore('-')
        .substringBefore('_')
        .lowercase()

    companion object {
        val US: DateTimeLocale = DateTimeLocale("en-US")

        fun fromTag(tag: String?): DateTimeLocale {
            return DateTimeLocale(tag?.trim()?.takeIf { it.isNotEmpty() } ?: US.tag)
        }
    }
}

internal expect fun platformDefaultLocaleTag(): String

private const val PATTERN_DATE_ONLY = "yyyy-MM-dd"
private const val PATTERN_UI_DATE_TIME = "yyyy-MM-dd HH:mm"
private const val PATTERN_TIME_24_HOUR = "HH:mm"
private const val PATTERN_API_ISO_UTC_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val PATTERN_SERVER_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
private const val PATTERN_CALENDAR_DAY_SLASH = "yyyy/MM/dd"
private const val PATTERN_CALENDAR_DAY_MONTH_SLASH = "dd/MM/yyyy"
private const val PATTERN_CALENDAR_MONTH_DAY = "d MMMM"
private const val PATTERN_CALENDAR_DAY_MONTH_YEAR = "d MMM yyyy"
private const val PATTERN_CALENDAR_DAY_MONTH_YEAR_TIME = "d MMM yyyy - h:mm a"

enum class EpochUnit {
    MILLISECONDS,
    SECONDS,
}

enum class DateFormatToken(val pattern: String) {
    UI_DATE_INPUT(PATTERN_DATE_ONLY),
    UI_DATE_TIME_INPUT(PATTERN_UI_DATE_TIME),
    TIME_24_HOUR(PATTERN_TIME_24_HOUR),
    API_ISO_UTC_MILLIS(PATTERN_API_ISO_UTC_MILLIS),
    SERVER_DATE_TIME(PATTERN_SERVER_DATE_TIME),
    CALENDAR_DAY_SLASH(PATTERN_CALENDAR_DAY_SLASH),
    CALENDAR_DAY_MONTH_SLASH(PATTERN_CALENDAR_DAY_MONTH_SLASH),
    CALENDAR_MONTH_DAY(PATTERN_CALENDAR_MONTH_DAY),
    CALENDAR_DAY_MONTH_YEAR(PATTERN_CALENDAR_DAY_MONTH_YEAR),
    CALENDAR_DAY_MONTH_YEAR_TIME(PATTERN_CALENDAR_DAY_MONTH_YEAR_TIME),
}

data class RelativeThresholds(
    val justNowMaxMillis: Long = 60_000L,
    val hourMaxMillis: Long = 3_600_000L,
    val dayMaxMillis: Long = 86_400_000L,
    val relativeMaxMillis: Long = 604_800_000L,
)

data class BusinessCalendarPolicy(
    val businessZone: TimeZone,
    val firstDayOfWeek: DayOfWeek,
    val weekendDays: Set<DayOfWeek>,
    val closingSoonThresholdMinutes: Long,
) {
    fun today(clockProvider: ClockProvider = SystemClockProvider): LocalDate {
        return kotlin.time.Instant
            .fromEpochMilliseconds(clockProvider.nowMillis())
            .toLocalDateTime(businessZone)
            .date
    }

    fun yesterday(clockProvider: ClockProvider = SystemClockProvider): LocalDate {
        return today(clockProvider).minus(DatePeriod(days = 1))
    }

    fun weekStart(date: LocalDate): LocalDate {
        var current = date
        while (current.dayOfWeek != firstDayOfWeek) {
            current = current.minus(DatePeriod(days = 1))
        }
        return current
    }

    fun currentWeekStart(clockProvider: ClockProvider = SystemClockProvider): LocalDate {
        return weekStart(today(clockProvider))
    }

    fun isWithinCurrentWeek(
        date: LocalDate,
        clockProvider: ClockProvider = SystemClockProvider,
    ): Boolean {
        return date >= currentWeekStart(clockProvider)
    }
}

object PlatformDateTimePolicy {
    val serverZone: TimeZone = TimeZone.of("Asia/Riyadh")
    val displayDefaultZone: TimeZone
        get() = TimeZone.currentSystemDefault()
    val epochUnit: EpochUnit = EpochUnit.MILLISECONDS

    const val uiDateInputPattern: String = PATTERN_DATE_ONLY
    const val uiDateTimeInputPattern: String = PATTERN_UI_DATE_TIME

    val uiDateInputPatterns: List<String> = listOf(
        DateFormatToken.UI_DATE_INPUT.pattern,
        "yyyy/MM/dd",
    )

    val uiDateTimeInputPatterns: List<String> = listOf(
        DateFormatToken.UI_DATE_TIME_INPUT.pattern,
    )

    val businessCalendar: BusinessCalendarPolicy
        get() = BusinessCalendarPolicy(
            businessZone = serverZone,
            firstDayOfWeek = DayOfWeek.SATURDAY,
            weekendDays = setOf(DayOfWeek.FRIDAY),
            closingSoonThresholdMinutes = 30L,
        )

    val fallbackPatterns: List<String> = listOf(
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSX",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/MM/dd HH:mm",
        "yyyy/MM/dd",
        "dd/MM/yyyy HH:mm",
        "dd/MM/yyyy",
    )

    val relativeThresholds: RelativeThresholds = RelativeThresholds()
}

object DateTimePolicy {
    val serverZone: TimeZone = PlatformDateTimePolicy.serverZone
    val displayDefaultZone: TimeZone
        get() = PlatformDateTimePolicy.displayDefaultZone
    val displayDefaultLocale: DateTimeLocale
        get() = DateTimeLocale.fromTag(platformDefaultLocaleTag())
    val parseLocale: DateTimeLocale = DateTimeLocale.US
    val epochUnit: EpochUnit = PlatformDateTimePolicy.epochUnit

    const val uiDateInputPattern: String = PlatformDateTimePolicy.uiDateInputPattern
    const val uiDateTimeInputPattern: String = PlatformDateTimePolicy.uiDateTimeInputPattern

    val uiDateInputPatterns: List<String> = PlatformDateTimePolicy.uiDateInputPatterns
    val uiDateTimeInputPatterns: List<String> = PlatformDateTimePolicy.uiDateTimeInputPatterns
    val businessCalendar: BusinessCalendarPolicy
        get() = PlatformDateTimePolicy.businessCalendar
    val fallbackPatterns: List<String> = PlatformDateTimePolicy.fallbackPatterns
    val relativeThresholds: RelativeThresholds = PlatformDateTimePolicy.relativeThresholds
}
