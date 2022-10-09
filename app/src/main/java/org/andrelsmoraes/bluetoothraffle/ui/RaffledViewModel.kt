package org.andrelsmoraes.bluetoothraffle.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetRaffledDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.ui.base.BaseViewModel
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class RaffledViewModel(
    private val getRaffledDevicesUseCase: GetRaffledDevicesUseCase
) : BaseViewModel() {

    companion object {
        private val TAG = RaffledViewModel::class.java.simpleName
    }

    private val _raffledData = MutableStateFlow<List<Device>>(mutableListOf())
    val raffledData = _raffledData.asStateFlow()

    fun refreshDevices() {
        viewModelScope.launch {
            getRaffledDevicesUseCase.run()
                .catch { error ->
                    Log.e(TAG, "Error: $error")
                    _uiState.value = UiStateEvent.Error
                }.collect { raffled ->
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
