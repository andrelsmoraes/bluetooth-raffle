package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository

class GetRaffledDevicesUseCase(
    private val raffledRepository: RaffledRepository
) : UseCase<Flow<Set<Device>>> {

    override fun run(): Flow<Set<Device>> {
        return raffledRepository.listDevices()
            .map {
                it.toSortedSet(DeviceSortByNewestRaffled)
            }
    }

    private object DeviceSortByNewestRaffled : Comparator<Device> {
        override fun compare(device: Device?, other: Device?): Int {
            val timeComparison = (other?.raffledTime ?: 0L).compareTo(device?.raffledTime ?: 0L)
            val nameComparison = if (timeComparison != 0) {
                timeComparison
            } else {
                other?.name.orEmpty().compareTo(device?.name.orEmpty())
            }
            return if (nameComparison != 0) {
                nameComparison
            } else {
                other?.address.orEmpty().compareTo(device?.address.orEmpty())
            }
        }
    }

}
