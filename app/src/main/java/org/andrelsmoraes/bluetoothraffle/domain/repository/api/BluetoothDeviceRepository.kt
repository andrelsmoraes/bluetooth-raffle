package org.andrelsmoraes.bluetoothraffle.domain.repository.api

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.model.api.BluetoothDevice
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

/**
 * Class to act as the BluetoothAdapter object from Android API,
 * returning mocked BluetoothDevice objects
 */
interface BluetoothDeviceRepository {

    fun startDiscovery(): Flow<List<BluetoothDevice>>

}
