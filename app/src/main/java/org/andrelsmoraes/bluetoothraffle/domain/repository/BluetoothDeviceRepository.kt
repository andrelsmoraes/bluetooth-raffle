package org.andrelsmoraes.bluetoothraffle.domain.repository

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds


interface BluetoothDeviceRepository {

    fun startDiscovery(timeout: Seconds): Flow<Unit>
    fun stopDiscovery(): Flow<Unit>

}
