package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.repository.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository

class StartDeviceSearchUseCase(
    private val bluetoothDeviceRepository: BluetoothDeviceRepository,
    private val preferencesRepository: PreferencesRepository
) : UseCase<Flow<Unit>> {

    override fun run(): Flow<Unit> = bluetoothDeviceRepository
        .startDiscovery(preferencesRepository.getDevicesSearchPeriod())

}
