package org.andrelsmoraes.bluetoothraffle.utils

sealed class UiStateEvent {

    object Empty : UiStateEvent()
    object Loading : UiStateEvent()
    object Error : UiStateEvent()
    object PermissionMissing : UiStateEvent()
    object Success : UiStateEvent()

}
