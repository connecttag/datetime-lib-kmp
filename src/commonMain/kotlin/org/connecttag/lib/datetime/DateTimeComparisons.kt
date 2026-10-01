package org.connecttag.lib.datetime

object DateTimeComparisons {

    fun isExpiredAt(targetMillis: Long?, nowMillis: Long): Boolean {
        return targetMillis != null && targetMillis <= nowMillis
    }

    fun isInPastStrict(targetMillis: Long?, nowMillis: Long): Boolean {
        return targetMillis != null && targetMillis < nowMillis
    }

    fun isWithinLast(targetMillis: Long?, windowMillis: Long, nowMillis: Long): Boolean {
        if (targetMillis == null || windowMillis <= 0L) return false
        val ageMillis = nowMillis - targetMillis
        return ageMillis in 0..windowMillis
    }
}
