package org.connecttag.lib.datetime

import kotlinx.datetime.TimeZone
import java.text.DateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

private val String?.isArabicLanguage: Boolean
    get() {
        val normalized = this?.trim()?.lowercase() ?: return false
        return normalized == "ar" || normalized == "arabic"
    }

internal object UiRelativeDateTimeFormatter {
    fun formatRelative(epochMillis: Long, nowMillis: Long, locale: Locale, zoneId: TimeZone): String {
        val isArabic = locale.language.isArabicLanguage
        val diff = nowMillis - epochMillis
        val thresholds = PlatformDateTimePolicy.relativeThresholds
        if (diff < 0L || diff < thresholds.justNowMaxMillis) {
            return if (isArabic) "الآن" else "Now"
        }

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            diff < thresholds.hourMaxMillis -> {
                if (isArabic) "منذ $minutes دقيقة" else "$minutes minutes ago"
            }
            diff < thresholds.dayMaxMillis -> {
                if (isArabic) "منذ $hours ساعة" else "$hours hours ago"
            }
            days == 1L -> {
                if (isArabic) "أمس" else "Yesterday"
            }
            diff < thresholds.relativeMaxMillis -> {
                if (isArabic) "منذ $days يوم" else "$days days ago"
            }
            else -> UiDateTimeFormatter.formatDate(epochMillis, DateFormat.MEDIUM, locale, zoneId)
        }
    }
}
