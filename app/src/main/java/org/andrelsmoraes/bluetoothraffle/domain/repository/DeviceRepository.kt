package org.andrelsmoraes.bluetoothraffle.domain.repository

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device

interface DeviceRepository {

    fun listDevices(): Flow<Set<Device>>

    fun observeSize(): Flow<Int>

}
