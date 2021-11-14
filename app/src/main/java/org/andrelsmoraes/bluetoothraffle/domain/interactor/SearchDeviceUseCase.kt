package org.andrelsmoraes.bluetoothraffle.domain.interactor

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import org.andrelsmoraes.bluetoothraffle.domain.mapper.BluetoothDeviceMapper
import org.andrelsmoraes.bluetoothraffle.domain.model.api.BluetoothDevice
import org.andrelsmoraes.bluetoothraffle.domain.repository.DeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.domain.repository.api.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.utils.downTo
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

class SearchDeviceUseCase(
    private val bluetoothDeviceRepository: BluetoothDeviceRepository,
    private val deviceRepository: DeviceRepository,
    private val bluetoothDeviceMapper: BluetoothDeviceMapper,
    private val preferencesRepository: PreferencesRepository
) {

    private var stopRequested = false

    fun getSearchTimeout() = preferencesRepository.getDevicesSearchPeriod()

    @FlowPreview
    fun run(): Flow<Boolean> {
        stopRequested = false
        return bluetoothDeviceRepository.startDiscovery()
            .flatMapConcat { mockRandomDevices(it, getSearchTimeout()) }
            .flatMapConcat { bluetoothDevices ->
                deviceRepository.addDevice(*bluetoothDevices
                    .map(bluetoothDeviceMapper::map)
                    .toTypedArray())
            }
    }

    fun stop() {
        stopRequested = true
    }

    private fun mockRandomDevices(
        originalDevices: List<BluetoothDevice>,
        timeout: Seconds
    ): Flow<List<BluetoothDevice>> = flow {
        val result = mutableListOf<BluetoothDevice>()
        (timeout downTo Seconds.ZERO).forEach { _ ->
            if (stopRequested) return@forEach

            result.add(originalDevices.random())
            delay(Seconds.ONE.toMillis())
        }
        emit(result)
        stopRequested = false
    }

}
