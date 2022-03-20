package com.sdn.teampredators.polima.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                auth.sendPasswordResetEmail(email).await()
            }.onSuccess {
            }.onFailure {
            }
        }
    }
}
