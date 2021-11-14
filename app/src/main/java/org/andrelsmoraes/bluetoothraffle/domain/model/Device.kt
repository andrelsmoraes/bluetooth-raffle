package org.andrelsmoraes.bluetoothraffle.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
open class Device(
    val name: String,
    val address: String,
    var raffledTime: Long? = null
) : Parcelable, Comparable<Device> {

    fun isRaffled() = raffledTime != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (address != other.address) return false

        return true
    }

    override fun hashCode() = address.hashCode()

    override fun compareTo(other: Device) = name.compareTo(other.name)

}
