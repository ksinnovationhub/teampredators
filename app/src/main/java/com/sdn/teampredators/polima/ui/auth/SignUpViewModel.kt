package com.sdn.teampredators.polima.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.auth.model.User
import com.sdn.teampredators.polima.utils.AuthenticationState
import com.sdn.teampredators.polima.utils.Constants.USERS_COLLECTION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _signUp = MutableSharedFlow<AuthenticationState>()
    val signUp = _signUp.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    fun doSignUp(email: String, password: String, fullName: String, nin: String, gender: String) {
        viewModelScope.launch(exceptionHandler) {
            _signUp.emit(AuthenticationState.Loading)
            kotlin.runCatching {
                val signUp = auth.createUserWithEmailAndPassword(email, password).await()
                signUp.user?.sendEmailVerification()?.await()
            }
                .onSuccess {
                    _signUp.emit(AuthenticationState.Success)
                    addUserInfoToDatabase(
                        userId = auth.uid!!,
                        email = email,
                        fullName = fullName,
                        nin = nin,
                        gender = gender
                    )
                }
                .onFailure { _signUp.emit(AuthenticationState.Error(it.localizedMessage)) }
        }
    }

    private suspend fun addUserInfoToDatabase(
        userId: String,
        email: String,
        fullName: String,
        nin: String,
        gender: String
    ) {
        kotlin.runCatching {
            val user = User(
                userId = userId,
                email = email,
                fullName = fullName,
                gender = gender,
                nin = nin
            )
            db.collection(USERS_COLLECTION).document(userId).set(user).await()
        }.onFailure { Timber.e(it) }
    }
}
