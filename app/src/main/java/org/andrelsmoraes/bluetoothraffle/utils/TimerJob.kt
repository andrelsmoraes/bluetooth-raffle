package org.andrelsmoraes.bluetoothraffle.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

class TimerJob(private val scope: CoroutineScope) {

    private var job: Job? = null
    private val _timerData = MutableStateFlow("")
    val timerData: StateFlow<String> = _timerData

    suspend fun startCountdown(seconds: Seconds) {
        job?.cancel()
        job = scope.launch(Dispatchers.Main) {
            (seconds downTo Seconds.ZERO).forEach {
                _timerData.value = it.formatToMinutesAndSeconds()
                delay(Seconds.ONE.toMillis())
            }
        }
    }

    fun stopCountDown() {
        job?.cancel()
    }

}
