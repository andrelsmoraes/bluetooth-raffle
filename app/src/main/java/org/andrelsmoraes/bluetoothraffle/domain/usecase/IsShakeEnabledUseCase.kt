package org.andrelsmoraes.bluetoothraffle.domain.usecase

import org.andrelsmoraes.bluetoothraffle.domain.base.UseCase
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository

class IsShakeEnabledUseCase(
    private val preferencesRepository: PreferencesRepository
) : UseCase<Boolean> {

    override fun run() = preferencesRepository.isShakeToRaffleEnabled()

}
