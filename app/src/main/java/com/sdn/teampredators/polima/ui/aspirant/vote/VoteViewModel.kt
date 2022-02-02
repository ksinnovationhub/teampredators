package com.sdn.teampredators.polima.ui.aspirant.vote

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
class VoteViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableLiveData<VoteStates>()
    val uiState: LiveData<VoteStates> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.postValue(VoteStates.Error(throwable))
        Timber.e(throwable)
    }

    private var politician: Politician? = null

    fun getPromises(politicianId: String) = viewModelScope.launch(exceptionHandler) {
        _uiState.postValue(VoteStates.Loading)
        kotlin.runCatching {
            val snapshot = db.collection(POLITICIAN_COLLECTION).document(politicianId).get().await()
            snapshot.toObject(Politician::class.java)
        }.onSuccess { response ->
            politician = response
            val promises = response!!.promises.filterNot {
                it.userIds.contains(auth.uid)
            }
            if (promises.isEmpty()) {
                _uiState.postValue(VoteStates.Success.EmptyPromises)
            } else {
                _uiState.postValue(VoteStates.Success.NonEmptyPromises(promises))
            }
        }.onFailure {
            _uiState.postValue(VoteStates.Error(it))
        }
    }

    fun voteUp(promiseId: String, politicianId: String) = viewModelScope.launch(exceptionHandler) {
        _uiState.postValue(VoteStates.Loading)
        if (politician != null) {

            val promises = politician!!.promises.toMutableList()
            val promise = promises.find {
                it.id == promiseId
            }
            if (promise != null) {
                val promisePosition = promises.indexOf(promise)
                val currentUserIds = promise.userIds.toMutableList()
                currentUserIds.add(auth.uid!!)
                val newCount = promise.upVoteCount + 1
                val promiseCopy = promise.copy(
                    userIds = currentUserIds,
                    upVoteCount = newCount
                )
                promises[promisePosition] = promiseCopy
                Timber.d(promises.toString())
                kotlin.runCatching {
                    db.collection(POLITICIAN_COLLECTION).document(politicianId)
                        .update("promises", promises).await()
                }.onSuccess {
                    _uiState.postValue(VoteStates.Success.VoteSuccess)
                }.onFailure {
                    _uiState.postValue(VoteStates.Error(it))
                }
            }
        }
    }

    fun voteDown(promiseId: String, politicianId: String) =
        viewModelScope.launch(exceptionHandler) {
            _uiState.postValue(VoteStates.Loading)
            if (politician != null) {

                val promises = politician!!.promises.toMutableList()
                val promise = promises.find {
                    it.id == promiseId
                }
                if (promise != null) {
                    val promisePosition = promises.indexOf(promise)
                    val currentUserIds = promise.userIds.toMutableList()
                    currentUserIds.add(auth.uid!!)
                    val newCount = promise.downVoteCount + 1
                    val promiseCopy = promise.copy(
                        userIds = currentUserIds,
                        downVoteCount = newCount
                    )
                    promises[promisePosition] = promiseCopy
                    Timber.d(promises.toString())
                    kotlin.runCatching {
                        db.collection(POLITICIAN_COLLECTION).document(politicianId)
                            .update("promises", promises).await()
                    }.onSuccess {
                        _uiState.postValue(VoteStates.Success.VoteSuccess)
                    }.onFailure {
                        _uiState.postValue(VoteStates.Error(it))
                    }
                }
            }
        }
}

sealed class VoteStates {
    object Loading : VoteStates()
    data class Error(val error: Throwable) : VoteStates()
    sealed class Success : VoteStates() {
        object EmptyPromises : Success()
        data class NonEmptyPromises(val data: List<Promise>) : Success()
        object VoteSuccess : Success()
    }
}
