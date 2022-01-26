package com.sdn.teampredators.polima.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sdn.teampredators.polima.utils.AuthenticationState
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class SignInViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _signIn = MutableSharedFlow<AuthenticationState>()
    val signIn = _signIn.asSharedFlow()

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    init {
        if (auth.currentUser != null && auth.currentUser?.isEmailVerified == true) {
            _action.value = GenericActions.Navigate(SignInFragmentDirections.toHomeFragment())
        }
    }

    fun doSignIn(email: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            _signIn.emit(AuthenticationState.Loading)
            kotlin.runCatching {
                auth.signInWithEmailAndPassword(email, password).await()
            }.fold(onSuccess = {
                if (it.user?.isEmailVerified == true) {
                    _action.value =
                        GenericActions.Navigate(SignInFragmentDirections.toHomeFragment())
                } else {
                    _signIn.emit(
                        AuthenticationState.Error(
                            "This email address has not been verified. " +
                                    "Please check your mail for the verification email"
                        )
                    )
                }
            }, onFailure = {
                _signIn.emit(
                    AuthenticationState.Error(it.localizedMessage)
                )
            })
        }
    }

    fun toSignUp() {
        _action.value = GenericActions.Navigate(SignInFragmentDirections.toSignUpFragment())
    }
}
