package org.andrelsmoraes.bluetoothraffle.utils

import androidx.databinding.ObservableField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

class ObservableTimer : ObservableField<String>() {

    suspend fun startCountdown(seconds: Seconds) {
        GlobalScope.launch(Dispatchers.Main) {
            (seconds downTo Seconds.ZERO).forEach {
                this@ObservableTimer.set(it.formatToMinutesAndSeconds())
                delay(Seconds.ONE.toMillis())
            }
        }
    }

}
