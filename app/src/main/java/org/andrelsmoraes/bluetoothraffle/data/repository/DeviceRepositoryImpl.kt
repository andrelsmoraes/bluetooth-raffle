package org.andrelsmoraes.bluetoothraffle.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import java.util.*

class DeviceRepositoryImpl : DeviceRepository {

    private val allDevices = mutableSetOf<Device>()

    override fun clearDevices(): Flow<Unit> = flow {
        emit(allDevices.clear())
    }

    override fun listDevices(): Flow<Set<Device>> = flow {
        emit(Collections.unmodifiableSet(allDevices))
    }

    override fun addDevice(vararg devices: Device): Flow<Boolean> = flow {
        emit(allDevices.addAll(devices))
    }

}
