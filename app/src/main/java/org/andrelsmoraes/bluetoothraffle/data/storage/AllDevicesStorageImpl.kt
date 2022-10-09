package org.andrelsmoraes.bluetoothraffle.data.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.storage.AllDevicesStorage
import java.util.*

open class AllDevicesStorageImpl : AllDevicesStorage {

    private val devices = mutableSetOf<Device>()
    private val _sizeFlow = MutableStateFlow<Int>(0)
    private val sizeFlow = _sizeFlow.asStateFlow()

    override fun addDevice(device: Device) {
        devices.add(device)
        _sizeFlow.tryEmit(devices.size)
    }

    override fun listDevices(): Set<Device> {
        val a = _sizeFlow.tryEmit(devices.size)
        return Collections.unmodifiableSet(devices)
    }

    override fun clearDevices() {
        devices.clear()
        _sizeFlow.tryEmit(devices.size)
    }

    override fun observeSize(): Flow<Int> = sizeFlow

}
