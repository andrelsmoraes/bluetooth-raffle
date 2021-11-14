package org.andrelsmoraes.bluetoothraffle.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository
import java.util.*

class RaffledRepositoryImpl : RaffledRepository {

    private val raffledDevices = mutableSetOf<Device>()

    override fun clearDevices(): Flow<Unit> = flow {
        emit(raffledDevices.clear())
    }

    override fun listDevices(): Flow<Set<Device>> = flow {
        emit(Collections.unmodifiableSet(raffledDevices))
    }

    override fun addDevice(vararg devices: Device): Flow<Boolean> = flow {
        emit(raffledDevices.addAll(devices))
    }

}
