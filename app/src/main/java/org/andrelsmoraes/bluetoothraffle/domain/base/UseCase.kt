package org.andrelsmoraes.bluetoothraffle.domain.base

interface UseCase<T> {
    fun run(): T
}