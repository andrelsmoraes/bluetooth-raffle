package org.andrelsmoraes.bluetoothraffle.domain.repository

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device

interface DeviceRepository {

    fun clearDevices(): Flow<Unit>

    fun listDevices(): Flow<Set<Device>>

    fun addDevice(vararg devices: Device): Flow<Boolean>

}
