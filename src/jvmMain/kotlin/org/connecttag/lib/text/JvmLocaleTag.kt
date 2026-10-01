package org.connecttag.lib.text

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

public fun defaultJvmLocaleTag(): String = Locale.getDefault().toLanguageTag()

public enum class JvmDateStyle {
    SHORT,
    MEDIUM,
}

public fun formatJvmDateWithDefaultLocale(
    epochMillis: Long,
    style: JvmDateStyle,
    timeZoneId: String,
): String {
    val dateStyle = when (style) {
        JvmDateStyle.SHORT -> DateFormat.SHORT
        JvmDateStyle.MEDIUM -> DateFormat.MEDIUM
    }
    return DateFormat.getDateInstance(dateStyle, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(timeZoneId)
    }.format(Date(epochMillis))
}

public fun formatJvmPatternWithDefaultLocale(
    epochMillis: Long,
    pattern: String,
    timeZoneId: String,
): String = SimpleDateFormat(pattern, Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone(timeZoneId)
    isLenient = false
}.format(Date(epochMillis))
