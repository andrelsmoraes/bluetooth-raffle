package org.andrelsmoraes.bluetoothraffle.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.SearchDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.ui.base.BaseViewModel
import org.andrelsmoraes.bluetoothraffle.utils.ObservableTimer
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class DevicesViewModel(
    private val searchDeviceUseCase: SearchDeviceUseCase,
    private val getRemainingDevicesUseCase: GetRemainingDevicesUseCase,
    private val raffleDeviceUseCase: RaffleDeviceUseCase,
) : BaseViewModel() {

    private val _devicesData = MutableStateFlow<List<Device>>(mutableListOf())
    val devicesData: StateFlow<List<Device>> = _devicesData
    private val _raffledData = MutableStateFlow<List<Device>>(mutableListOf())
    val raffledData: StateFlow<List<Device>> = _raffledData
    val searchingTimer = ObservableTimer()

    private var searchJob: Job? = null

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun startDevicesSearch() {
        searchJob = viewModelScope.launch {
            searchDeviceUseCase.run()
                .onStart {
                    _uiState.value = UiStateEvent.Loading
                    searchingTimer.startCountdown(searchDeviceUseCase.getSearchTimeout())
                }
                .catch {
                    _uiState.value = UiStateEvent.Error
                }.flatMapConcat {
                    getRemainingDevicesUseCase.run()
                }.collect { devices ->
                    _uiState.value =
                        if (devices.isNotEmpty()) UiStateEvent.Success else UiStateEvent.Empty
                    _devicesData.value = devices.toList()
                }
        }
    }

    fun stopDevicesSearch() {
        if (searchJob?.isActive == true) {
            searchDeviceUseCase.stop()
        }
    }

    @FlowPreview
    fun raffle() {
        viewModelScope.launch {
            raffleDeviceUseCase.run()
                .catch { e ->
                    Log.e("TAG", "Error: $e") //TODO error
                    _uiState.value = UiStateEvent.Error
                }.collect { raffledDevices ->
                    _raffledData.value = raffledDevices.toList()
                }
        }
    }

    fun refreshDevices() {
        viewModelScope.launch {
            getRemainingDevicesUseCase.run()
                .catch {
                    _uiState.value = UiStateEvent.Error
                }.collect { devices ->
                    //TODO loading?

                    if (devices.isEmpty()) {
                        _uiState.value = UiStateEvent.Empty
                    } else if (uiState.value !is UiStateEvent.Success) {
                        _uiState.value = UiStateEvent.Success
                    }

                    _devicesData.value = devices.toList()
                }
        }
    }

}
