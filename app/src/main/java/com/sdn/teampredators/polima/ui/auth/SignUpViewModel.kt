package com.sdn.teampredators.polima.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sdn.teampredators.polima.utils.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class SignUpViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _signUp = MutableSharedFlow<AuthenticationState>()
    val signUp = _signUp.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    fun doSignUp(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            _signUp.emit(AuthenticationState.Loading)
            kotlin.runCatching {
                val signUp = auth.createUserWithEmailAndPassword(email, password).await()
                signUp.user?.sendEmailVerification()?.await()
            }
                .onSuccess { _signUp.emit(AuthenticationState.Success) }
                .onFailure { _signUp.emit(AuthenticationState.Error(it.localizedMessage)) }
        }
    }
}
