package com.sdn.teampredators.polima.ui.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.auth.model.User
import com.sdn.teampredators.polima.utils.Constants.USERS_COLLECTION
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userResponse: MutableLiveData<ProfileState> = MutableLiveData()
    val userResponse: LiveData<ProfileState> = _userResponse

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _userResponse.postValue(ProfileState.Error(throwable))
        Timber.e(throwable)
    }

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch(exceptionHandler) {
            _userResponse.postValue(ProfileState.Loading)
            kotlin.runCatching {
                firestore.collection(USERS_COLLECTION).document(auth.uid ?: "").get().await()
                    .toObject(User::class.java)
            }.onSuccess {
                Timber.d(it.toString())
                _userResponse.postValue(ProfileState.Success(it))
            }.onFailure {
                Timber.e(it)
                _userResponse.postValue(ProfileState.Error(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                auth.signOut()
            }.onSuccess {
                // TODO: Navigate to auth screen
            }.onFailure {

            }
        }
    }
}

sealed class ProfileState {
    data class Success(val user: User?): ProfileState()
    data class Error(val error: Throwable): ProfileState()
    object Loading: ProfileState()
}
