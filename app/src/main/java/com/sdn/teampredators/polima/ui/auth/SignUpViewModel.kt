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

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signUp = MutableSharedFlow<AuthenticationState>()
    val signUp = _signUp.asSharedFlow()

    suspend fun doSignUp(email: String, password: String) {
        _signUp.emit(AuthenticationState.Loading)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            viewModelScope.launch {
                if (task.isSuccessful)
                // Create account was successfull, update UI with the signed-in user's information
                    _signUp.emit(AuthenticationState.Success)
                else
                // If Create account failed, display a message to the user.
                    _signUp.emit(AuthenticationState.Error(task.exception?.localizedMessage))

            }
        }
    }

}
