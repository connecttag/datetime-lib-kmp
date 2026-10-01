package org.connecttag.lib.datetime

/**
 * Interface to provide the current time.
 * Essential for testing time-dependent logic.
 */
interface ClockProvider {
    /**
     * Returns the current time in milliseconds since epoch.
     */
    fun nowMillis(): Long
}

/**
 * Platform clock implementation.
 */
object SystemClockProvider : ClockProvider {
    override fun nowMillis(): Long = systemNowMillis()
}

internal expect fun systemNowMillis(): Long

/**
 * A fake clock provider for testing purposes.
 */
class FakeClockProvider(
    initialMillis: Long = 0L,
) : ClockProvider {
    private var currentMillis: Long = initialMillis

    override fun nowMillis(): Long = currentMillis

    fun setNowMillis(value: Long) {
        currentMillis = value
    }

    fun advanceBy(deltaMillis: Long) {
        currentMillis += deltaMillis
    }
}
