package org.andrelsmoraes.bluetoothraffle.domain.storage

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device

interface AllDevicesStorage {

    fun addDevice(device: Device)

    fun listDevices(): Set<Device>

    fun clearDevices()

    fun observeSize(): Flow<Int>

}