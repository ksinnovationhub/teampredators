package com.sdn.teampredators.polima.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sdn.teampredators.polima.utils.AuthenticationState
import com.sdn.teampredators.polima.utils.SignInActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signIn = MutableSharedFlow<AuthenticationState>()
    val signIn = _signIn.asSharedFlow()

    private val _action = SingleLiveEvent<SignInActions>()
    val action: LiveData<SignInActions> = _action


    suspend fun doSignIn(email: String, password: String) {
        _signIn.emit(AuthenticationState.Loading)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            viewModelScope.launch {
                if (task.isSuccessful)
                // Sign in success, update UI with the signed-in user's information
                    _signIn.emit(AuthenticationState.Success)
                else
                // If sign in fails, display a message to the user.
                    _signIn.emit(AuthenticationState.Error(task.exception?.localizedMessage))

            }
        }
    }

    fun toSignUp() {
        _action.value = SignInActions.Navigate(SignInFragmentDirections.toSignUpFragment())
    }
}