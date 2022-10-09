package org.andrelsmoraes.bluetoothraffle.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository
import org.andrelsmoraes.bluetoothraffle.domain.storage.RaffledDevicesStorage

open class RaffledRepositoryImpl(
    private val raffledDevicesStorageImpl: RaffledDevicesStorage
) : RaffledRepository {

    override fun addDevice(vararg devices: Device): Flow<Unit> = flow {
        devices.forEach(raffledDevicesStorageImpl::addDevice)
        emit(Unit)
    }

    override fun listDevices(): Flow<Set<Device>> = flow {
        emit(raffledDevicesStorageImpl.listDevices())
    }

    override fun observeSize(): Flow<Int> = raffledDevicesStorageImpl.observeSize()

}