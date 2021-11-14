package org.andrelsmoraes.bluetoothraffle.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRaffledDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.GetRemainingDevicesUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.RaffleDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.interactor.SearchDeviceUseCase
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.utils.ObservableTimer
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

class RaffledViewModel(private val getRaffledDevicesUseCase: GetRaffledDevicesUseCase) :
    ViewModel() {

    val uiState: MutableLiveData<UiStateEvent> = MutableLiveData(UiStateEvent.Empty)
    val raffledData: MutableLiveData<List<Device>> = MutableLiveData()

    fun refreshDevices() {
        viewModelScope.launch {
            getRaffledDevicesUseCase.run()
                .catch {
                    uiState.value = UiStateEvent.Error

                    //TODO error dialog or component - reuse on Device list

                }.collect { raffled ->
                    //TODO loading?

                    if (raffled.isEmpty()) {
                        uiState.value = UiStateEvent.Empty
                    } else if (uiState.value !is UiStateEvent.Success) {
                        uiState.value = UiStateEvent.Success
                    }

                    raffledData.value = raffled.toList()
                }
        }
    }

}
