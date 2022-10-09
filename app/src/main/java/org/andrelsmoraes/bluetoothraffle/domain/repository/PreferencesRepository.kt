package org.andrelsmoraes.bluetoothraffle.domain.repository

import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

interface PreferencesRepository {

    fun getDevicesSearchPeriod(): Seconds

    fun getRaffleQuantityPerRound(): Int

    fun isShakeToRaffleEnabled(): Boolean

}
