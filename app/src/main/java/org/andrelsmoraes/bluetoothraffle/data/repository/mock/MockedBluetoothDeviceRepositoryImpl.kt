package org.andrelsmoraes.bluetoothraffle.data.repository.mock

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.repository.BluetoothDeviceRepository
import org.andrelsmoraes.bluetoothraffle.domain.storage.AllDevicesStorage
import org.andrelsmoraes.bluetoothraffle.utils.downTo
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

class MockedBluetoothDeviceRepositoryImpl(
    context: Context,
    private val allDevicesStorage: AllDevicesStorage
) : BluetoothDeviceRepository {

    private var stopRequested = false
    private val mockedDevices = context.assets.open(DEVICES_DATA_FILE).bufferedReader().use {
        Json.decodeFromString<Set<Device>>(it.readText())
    }
    override fun startDiscovery(timeout: Seconds): Flow<Unit> = flow {
        (timeout downTo Seconds.ZERO).forEach { _ ->
            if (stopRequested) return@forEach

            allDevicesStorage.addDevice(mockedDevices.random())
            delay(Seconds.ONE.toMillis())
        }
        emit(Unit)
        stopRequested = false
    }

    override fun stopDiscovery(): Flow<Unit> = flow {
        stopRequested = true
        emit(Unit)
    }

    companion object {
        private const val DEVICES_DATA_FILE = "devices_data.json"
    }

}
