package org.andrelsmoraes.bluetoothraffle.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class DevicesSize(
    val raffledSize: Int,
    val remainingSize: Int
) : Parcelable