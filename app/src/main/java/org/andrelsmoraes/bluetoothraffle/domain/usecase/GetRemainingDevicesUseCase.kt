package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository

class GetRemainingDevicesUseCase(
    private val deviceRepository: DeviceRepository,
    private val raffledRepository: RaffledRepository
) : UseCase<Flow<Set<Device>>> {

    override fun run(): Flow<Set<Device>> {
        return deviceRepository.listDevices()
            .zip(raffledRepository.listDevices(), ::mapRemaining)
            .map { it }
    }

    private fun mapRemaining(
        allDevices: Set<Device>,
        raffledDevices: Set<Device>
    ): Set<Device> = allDevices - raffledDevices

}
