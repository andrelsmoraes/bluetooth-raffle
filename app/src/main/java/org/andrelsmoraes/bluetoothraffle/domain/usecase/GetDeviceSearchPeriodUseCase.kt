package org.andrelsmoraes.bluetoothraffle.domain.usecase

import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds

class GetDeviceSearchPeriodUseCase(
    private val preferencesRepository: PreferencesRepository
) : UseCase<Seconds> {

    override fun run() = preferencesRepository.getDevicesSearchPeriod()

}
