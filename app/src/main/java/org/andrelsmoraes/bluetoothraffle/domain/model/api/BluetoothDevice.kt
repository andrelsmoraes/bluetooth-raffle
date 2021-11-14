package org.andrelsmoraes.bluetoothraffle.domain.model.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Class to act as a BluetoothDevice object from Android API
 */
@Serializable
open class BluetoothDevice(
    val name: String,
    val address: String,
    @Transient val type: Int = 0,
    @Transient val uuids: List<String> = emptyList()
)
