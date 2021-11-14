package org.andrelsmoraes.bluetoothraffle.utils.time

class SecondsProgression(
    override val start: Seconds,
    override val endInclusive: Seconds,
    private val steps: Int = 1
) : Iterable<Seconds>, ClosedRange<Seconds> {

    override fun iterator() = SecondsIterator(start, endInclusive, steps)

}
