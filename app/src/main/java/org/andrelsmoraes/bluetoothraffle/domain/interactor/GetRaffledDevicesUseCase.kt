package org.andrelsmoraes.bluetoothraffle.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository

class GetRaffledDevicesUseCase(private val raffledRepository: RaffledRepository) {

    fun run(): Flow<Set<Device>> {
        return raffledRepository.listDevices()
            .map { it.toSortedSet() }
    }

}
