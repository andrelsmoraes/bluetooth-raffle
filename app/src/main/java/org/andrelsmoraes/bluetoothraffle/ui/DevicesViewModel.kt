package org.andrelsmoraes.bluetoothraffle.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.SearchDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.utils.ObservableTimer
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class DevicesViewModel(
    private val searchDeviceUseCase: SearchDeviceUseCase,
    private val getRemainingDevicesUseCase: GetRemainingDevicesUseCase,
    private val raffleDeviceUseCase: RaffleDeviceUseCase,
) : ViewModel() {

    val uiState: MutableLiveData<UiStateEvent> = MutableLiveData(UiStateEvent.Empty)
    val devicesData: MutableLiveData<List<Device>> = MutableLiveData()
    val raffledData: MutableLiveData<List<Device>> = MutableLiveData()
    val searchingTimer = ObservableTimer()

    private var searchJob: Job? = null

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun startDevicesSearch() {
        searchJob = viewModelScope.launch {
            searchDeviceUseCase.run()
                .onStart {
                    uiState.value = UiStateEvent.Loading
                    searchingTimer.startCountdown(searchDeviceUseCase.getSearchTimeout())
                }
                .catch {
                    uiState.value = UiStateEvent.Error
                }.flatMapConcat {
                    getRemainingDevicesUseCase.run()
                }.collect { devices ->
                    uiState.value =
                        if (devices.isNotEmpty()) UiStateEvent.Success else UiStateEvent.Empty
                    devicesData.value = devices.toList()
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
                    uiState.value = UiStateEvent.Error
                }.collect { raffledDevices ->
                    raffledData.value = raffledDevices.toList()
                }
        }
    }

    fun refreshDevices() {
        viewModelScope.launch {
            getRemainingDevicesUseCase.run()
                .catch {
                    uiState.value = UiStateEvent.Error
                }.collect { devices ->
                    //TODO loading?

                    if (devices.isEmpty()) {
                        uiState.value = UiStateEvent.Empty
                    } else if (uiState.value !is UiStateEvent.Success) {
                        uiState.value = UiStateEvent.Success
                    }

                    devicesData.value = devices.toList()
                }
        }
    }

}
