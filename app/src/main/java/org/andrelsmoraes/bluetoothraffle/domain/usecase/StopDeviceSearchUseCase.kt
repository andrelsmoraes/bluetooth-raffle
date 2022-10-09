package org.andrelsmoraes.bluetoothraffle.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.repository.BluetoothDeviceRepository

class StopDeviceSearchUseCase(
    private val bluetoothDeviceRepository: BluetoothDeviceRepository
) : UseCase<Flow<Unit>> {

    override fun run(): Flow<Unit> = bluetoothDeviceRepository.stopDiscovery()

}
