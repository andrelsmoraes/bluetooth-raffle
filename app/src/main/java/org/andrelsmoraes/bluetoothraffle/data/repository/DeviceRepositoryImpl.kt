package org.andrelsmoraes.bluetoothraffle.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.storage.AllDevicesStorage

open class DeviceRepositoryImpl(
    private val allDevicesStorageImpl: AllDevicesStorage
) : DeviceRepository {

    override fun listDevices(): Flow<Set<Device>> = flow {
        emit(allDevicesStorageImpl.listDevices())
    }

    override fun observeSize(): Flow<Int> = allDevicesStorageImpl.observeSize()

}
