package org.andrelsmoraes.bluetoothraffle.utils.time

class Seconds(
    val value: Int
) : Comparable<Seconds> {

    fun toMillis() = value * ONE_IN_MILLIS

    override fun compareTo(other: Seconds) = this.value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Seconds

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }

    companion object {
        private const val ONE_IN_MILLIS = 1000L

        val ZERO = Seconds(0)
        val ONE = Seconds(1)
    }

}
