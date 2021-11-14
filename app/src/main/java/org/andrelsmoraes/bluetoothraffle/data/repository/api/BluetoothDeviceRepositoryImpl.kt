package org.andrelsmoraes.bluetoothraffle.data.repository.api

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.andrelsmoraes.bluetoothraffle.domain.model.api.BluetoothDevice
import org.andrelsmoraes.bluetoothraffle.domain.repository.api.BluetoothDeviceRepository

class BluetoothDeviceRepositoryImpl(private val context: Context) : BluetoothDeviceRepository {

    override fun startDiscovery(): Flow<List<BluetoothDevice>> = flow {
        context.assets.open(DEVICES_DATA_FILE).bufferedReader().use {
            emit(Json.decodeFromString<List<BluetoothDevice>>(it.readText()))
        }
    }

    companion object {
        private const val DEVICES_DATA_FILE = "devices_data.json"
    }

}
