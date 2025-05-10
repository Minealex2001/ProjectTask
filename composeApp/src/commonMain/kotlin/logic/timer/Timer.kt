package logic.timer

class Timer {
    var isRunning: Boolean = false
        private set // Make setter private if only controlled by class methods

    // Stores the accumulated time when the timer is paused.
    private var accumulatedTimeWhenPaused: Long = 0

    // The system time when the timer was last started or resumed.
    // This is used to calculate the current session's duration.
    private var currentSessionStartTime: Long = 0

    /**
     * The total elapsed time.
     * If the timer is running, this calculates the current elapsed time.
     * If the timer is paused, this returns the total time accumulated.
     */
    val elapsedTime: Long
        get() {
            return if (isRunning) {
                // Calculate duration of current session and add previously accumulated time.
                // currentSessionStartTime is set in start() to account for accumulatedTimeWhenPaused.
                System.currentTimeMillis() - currentSessionStartTime
            } else {
                accumulatedTimeWhenPaused
            }
        }

    fun start() {
        if (!isRunning) {
            isRunning = true
            // Set the start time for the current session, adjusting for any previously paused time.
            // This ensures elapsedTime correctly reflects the total, including past sessions.
            currentSessionStartTime = System.currentTimeMillis() - accumulatedTimeWhenPaused
        }
    }

    fun pause() {
        if (isRunning) {
            isRunning = false
            // Store the total accumulated time up to this pause.
            accumulatedTimeWhenPaused = System.currentTimeMillis() - currentSessionStartTime
        }
    }

    fun reset() {
        isRunning = false
        accumulatedTimeWhenPaused = 0
        currentSessionStartTime = 0 // Reset, will be recalculated on next start
    }
}