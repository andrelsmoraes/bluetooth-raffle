package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.RaffledRepository
import java.util.*

class RaffleDeviceUseCase(
    private val deviceRepository: DeviceRepository,
    private val raffledRepository: RaffledRepository,
    private val preferencesRepository: PreferencesRepository
) : UseCase<Flow<Set<Device>>> {

    @FlowPreview
    override fun run(): Flow<Set<Device>> {
        return deviceRepository.listDevices()
            .zip(raffledRepository.listDevices(), this::getNotRaffled)
            .map(this::raffle)
            .flatMapConcat { raffledDevices ->
                raffledRepository.addDevice(*raffledDevices.toTypedArray())
                    .map { raffledDevices }
            }
    }

    private fun getNotRaffled(
        allDevices: Set<Device>,
        raffledDevices: Set<Device>
    ): Set<Device> = allDevices - raffledDevices

    private fun raffle(devicesNotRaffled: Set<Device>): Set<Device> {
        val raffledTime = Date().time
        val raffled = mutableSetOf<Device>()
        val raffleLimit =
            if (devicesNotRaffled.size < preferencesRepository.getRaffleQuantityPerRound()) {
                devicesNotRaffled.size
            } else {
                preferencesRepository.getRaffleQuantityPerRound()
            }

        while (raffled.size < raffleLimit) {
            raffled.add(devicesNotRaffled.shuffled().first().also { it.raffledTime = raffledTime })
        }

        return raffled
    }

}
