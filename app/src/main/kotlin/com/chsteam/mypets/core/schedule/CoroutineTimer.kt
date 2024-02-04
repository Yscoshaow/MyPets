package com.chsteam.mypets.core.schedule

import kotlinx.coroutines.*

class CoroutineTimer {

    private var laterJob: Job? = null
    private var timerJob: Job? = null

    fun runTaskLater(time: Long, action: suspend () -> Unit) {
        laterJob = CoroutineScope(Dispatchers.Main).launch {
            delay(time)
            action()
        }
    }

    fun runTaskTimer(delay: Long, period: Long, action: suspend () -> Unit) {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            delay(delay)
            while (isActive) {
                action()
                delay(period)
            }
        }
    }

    fun cancelTask() {
        laterJob?.cancel()
        timerJob?.cancel()
    }

    fun cancelTimer() {
        timerJob?.cancel()
    }

    fun cancelLater() {
        laterJob?.cancel()
    }
}
