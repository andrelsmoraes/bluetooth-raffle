package org.andrelsmoraes.bluetoothraffle.domain.repository

import kotlinx.coroutines.flow.Flow
import org.andrelsmoraes.bluetoothraffle.domain.model.Device

interface RaffledRepository : DeviceRepository {

    fun addDevice(vararg devices: Device): Flow<Unit>

}
