package org.andrelsmoraes.bluetoothraffle.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.andrelsmoraes.bluetoothraffle.domain.model.DevicesSize
import org.andrelsmoraes.bluetoothraffle.domain.usecase.ObserveDevicesSizeUseCase
import org.andrelsmoraes.bluetoothraffle.ui.base.BaseViewModel

class MainViewModel(
    private val observeDevicesSizeUseCase: ObserveDevicesSizeUseCase
) : BaseViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val _sizeData = MutableStateFlow(DevicesSize(0,0))
    val sizeData = _sizeData.asStateFlow()
    private val _foundData = MutableStateFlow(Unit)
    val foundData = _foundData.asStateFlow()

    @FlowPreview
    fun observeSizes() {
        observeDevicesSizeUseCase.run()
            .catch { error ->
                Log.e(TAG, "Error: $error")
            }
            .onEach { devicesSize ->
                _sizeData.value = devicesSize
            }
            .launchIn(viewModelScope)
    }

}
