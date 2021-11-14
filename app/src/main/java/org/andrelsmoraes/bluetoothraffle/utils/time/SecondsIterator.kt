package org.andrelsmoraes.bluetoothraffle.utils.time

class SecondsIterator(first: Seconds, last: Seconds, val step: Int) : Iterator<Seconds> {

    private val finalElement = last
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next = if (hasNext) first else finalElement

    override fun hasNext(): Boolean = hasNext

    override fun next(): Seconds {
        val value = next
        if (value == finalElement) {
            if (!hasNext) throw kotlin.NoSuchElementException()
            hasNext = false
        }
        else {
            next = Seconds(next.value + step)
        }
        return value
    }

}
