package org.andrelsmoraes.bluetoothraffle.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent

abstract class BaseViewModel : ViewModel() {

    protected val _uiState= MutableStateFlow<UiStateEvent>(UiStateEvent.Empty)
    val uiState: StateFlow<UiStateEvent> = _uiState

}