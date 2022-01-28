package com.sdn.teampredators.polima.ui.aspirant.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.utils.Constants.POLITICIAN_COLLECTION
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableLiveData<VerifyPromiseStates>()
    val uiState: LiveData<VerifyPromiseStates> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.postValue(VerifyPromiseStates.Error(throwable))
        Timber.e(throwable)
    }

    fun getPromises(politicianId: String) = viewModelScope.launch(exceptionHandler) {
        _uiState.postValue(VerifyPromiseStates.Loading)
        kotlin.runCatching {
            val snapshot = db.collection(POLITICIAN_COLLECTION).document(politicianId).get().await()
            snapshot.toObject(Politician::class.java)
        }.onSuccess { response ->
            val promises = response!!.promises.filter {
                it.userIds.contains(auth.uid)
            }
            if (promises.isEmpty()) {
                _uiState.postValue(VerifyPromiseStates.Success.EmptyPromises)
            } else {
                _uiState.postValue(VerifyPromiseStates.Success.NonEmptyPromises(promises))
            }
        }.onFailure {
            _uiState.postValue(VerifyPromiseStates.Error(it))
        }
    }
}

sealed class VerifyPromiseStates {
    object Loading : VerifyPromiseStates()
    data class Error(val error: Throwable) : VerifyPromiseStates()
    sealed class Success : VerifyPromiseStates() {
        object EmptyPromises : Success()
        data class NonEmptyPromises(val data: List<Promise>) : Success()
    }
}
