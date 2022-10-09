package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.DevicesSize
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository

class ObserveDevicesSizeUseCase(
    private val deviceRepository: DeviceRepository,
    private val raffledRepository: RaffledRepository
) : UseCase<Flow<DevicesSize>> {

    @FlowPreview
    override fun run(): Flow<DevicesSize> {
        return raffledRepository.observeSize()
            .combine(deviceRepository.observeSize(), ::mapRemainingSize)
            .debounce(500)
    }

    private fun mapRemainingSize(
        raffledSize: Int,
        allDevicesSize: Int
    ) = DevicesSize(
        raffledSize = raffledSize,
        remainingSize = allDevicesSize - raffledSize
    )

}
