package com.sdn.teampredators.polima.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.utils.DummyNotificationData.getNotification

class NotificationViewModel : ViewModel() {

    private val _uiState = MutableLiveData<NotificationState>()
    val uiState: LiveData<NotificationState> = _uiState

    init {
        val notifications = mutableListOf<NotificationModel>()
        repeat(20) {
            notifications.add(getNotification())
        }
        _uiState.value = NotificationState.Content(notifications)
    }
}

sealed class NotificationState {
    data class Content(val notifications: List<NotificationModel>) : NotificationState()
}
