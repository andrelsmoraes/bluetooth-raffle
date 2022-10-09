package org.andrelsmoraes.bluetoothraffle.data.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import org.andrelsmoraes.bluetoothraffle.domain.model.Device

object BluetoothDeviceMapper {

    @SuppressLint("MissingPermission")
    fun map(from: BluetoothDevice) = Device(from.name.orEmpty(), from.address.orEmpty())

}
