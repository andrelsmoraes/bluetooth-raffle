package org.andrelsmoraes.bluetoothraffle.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetDeviceSearchPeriodUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.IsShakeEnabledUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.StartDeviceSearchUseCase
import org.andrelsmoraes.bluetoothraffle.domain.usecase.StopDeviceSearchUseCase
import org.andrelsmoraes.bluetoothraffle.ui.base.BaseViewModel
import org.andrelsmoraes.bluetoothraffle.utils.TimerJob
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class DevicesViewModel(
    private val startDeviceSearchUseCase: StartDeviceSearchUseCase,
    private val stopDeviceSearchUseCase: StopDeviceSearchUseCase,
    private val getDeviceSearchPeriodUseCase: GetDeviceSearchPeriodUseCase,
    private val getRemainingDevicesUseCase: GetRemainingDevicesUseCase,
    private val raffleDeviceUseCase: RaffleDeviceUseCase,
    private val isShakeEnabledUseCase: IsShakeEnabledUseCase
) : BaseViewModel() {

    companion object {
        private val TAG = DevicesViewModel::class.java.simpleName
    }

    private val _devicesData = MutableStateFlow<List<Device>>(mutableListOf())
    val devicesData = _devicesData.asStateFlow()
    private val _raffledData = MutableStateFlow<List<Device>>(mutableListOf())
    val raffledData = _raffledData.asStateFlow()
    private val timerJob = TimerJob(viewModelScope)
    val timerData: StateFlow<String> = timerJob.timerData

    private var searchJob: Job? = null

    @FlowPreview
    fun startDevicesSearch() {
        searchJob = viewModelScope.launch {
            startDeviceSearchUseCase.run()
                .onStart {
                    _uiState.value = UiStateEvent.Loading
                    timerJob.startCountdown(getDeviceSearchPeriodUseCase.run())
                }
                .catch { error ->
                    Log.e(TAG, "Error: $error")
                    timerJob.stopCountDown()
                    _uiState.value = UiStateEvent.Error
                }.flatMapConcat {
                    getRemainingDevicesUseCase.run()
                }.collect { devices ->
                    timerJob.stopCountDown()
                    _uiState.value =
                        if (devices.isNotEmpty()) UiStateEvent.Success else UiStateEvent.Empty
                    _devicesData.value = devices.toList()
                }
        }
    }

    @FlowPreview
    fun stopDevicesSearch() {
        if (searchJob?.isActive == true) {
            timerJob.stopCountDown()
            viewModelScope.launch {
                stopDeviceSearchUseCase.run().collect()
            }
        }
    }

    @FlowPreview
    fun raffle() {
        viewModelScope.launch {
            raffleDeviceUseCase.run()
                .catch { e ->
                    Log.e(TAG, "Error: $e")
                    _uiState.value = UiStateEvent.Error
                }.collect { raffledDevices ->
                    _raffledData.value = raffledDevices.toList()
                }
        }
    }

    fun refreshDevices() {
        viewModelScope.launch {
            getRemainingDevicesUseCase.run()
                .catch { error ->
                    Log.e(TAG, "Error: $error")
                    _uiState.value = UiStateEvent.Error
                }.collect { devices ->
                    if (devices.isEmpty()) {
                        _uiState.value = UiStateEvent.Empty
                    } else if (uiState.value !is UiStateEvent.Success) {
                        _uiState.value = UiStateEvent.Success
                    }
                    _devicesData.value = devices.toList()
                }
        }
    }

    fun onPermissionError() {
        _uiState.value = UiStateEvent.PermissionMissing
    }

    fun isShakeEnabled() = isShakeEnabledUseCase.run()

    @FlowPreview
    override fun onCleared() {
        super.onCleared()
        stopDevicesSearch()
        timerJob.stopCountDown()
    }

}
