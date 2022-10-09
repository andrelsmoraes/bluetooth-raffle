package org.andrelsmoraes.bluetoothraffle.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Device(
    val name: String,
    val address: String,
    var raffledTime: Long? = null
) : Parcelable {

    fun isRaffled() = raffledTime != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (address != other.address) return false

        return true
    }

    override fun hashCode() = address.hashCode()

}
