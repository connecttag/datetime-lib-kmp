package org.connecttag.lib.datetime

/** Canonical epoch conversion helpers for public contracts. */
object EpochTime {
    private const val EPOCH_MILLISECONDS_THRESHOLD = 10_000_000_000L

    /**
     * Converts a positive epoch value to milliseconds.
     *
     * Public APIs historically exposed both epoch seconds and epoch milliseconds.
     * Values below the threshold are seconds; larger values are already milliseconds.
     */
    fun normalizeMillis(value: Long?): Long? {
        val positiveValue = value?.takeIf { it > 0L } ?: return null
        return if (positiveValue < EPOCH_MILLISECONDS_THRESHOLD) {
            positiveValue * 1_000L
        } else {
            positiveValue
        }
    }
}
