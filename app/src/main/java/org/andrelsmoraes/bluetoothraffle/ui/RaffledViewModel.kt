package org.andrelsmoraes.bluetoothraffle.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRaffledDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.ui.base.BaseViewModel
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class RaffledViewModel(
    private val getRaffledDevicesUseCase: GetRaffledDevicesUseCase
) : BaseViewModel() {

    private val _raffledData = MutableStateFlow<List<Device>>(mutableListOf())
    val raffledData: StateFlow<List<Device>> = _raffledData

    fun refreshDevices() {
        viewModelScope.launch {
            getRaffledDevicesUseCase.run()
                .catch {
                    _uiState.value = UiStateEvent.Error

                    //TODO error dialog or component - reuse on Device list

                }.collect { raffled ->
                    //TODO loading?

                    if (raffled.isEmpty()) {
                        _uiState.value = UiStateEvent.Empty
                    } else if (uiState.value !is UiStateEvent.Success) {
                        _uiState.value = UiStateEvent.Success
                    }

                    _raffledData.value = raffled.toList()
                }
        }
    }

}
