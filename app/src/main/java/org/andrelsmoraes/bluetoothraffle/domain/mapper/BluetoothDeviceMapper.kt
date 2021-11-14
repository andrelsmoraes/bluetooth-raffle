package org.andrelsmoraes.bluetoothraffle.domain.mapper

import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.model.api.BluetoothDevice

class BluetoothDeviceMapper {

    fun map(from: BluetoothDevice) = Device(from.name, from.address)

}
