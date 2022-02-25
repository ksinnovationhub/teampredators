package com.sdn.teampredators.polima.ui.aspirant.promise_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.aspirant.promise_details.model.Comment
import com.sdn.teampredators.polima.ui.auth.model.User
import com.sdn.teampredators.polima.utils.Constants.COMMENTS_COLLECTION
import com.sdn.teampredators.polima.utils.Constants.USERS_COLLECTION
import com.sdn.teampredators.polima.utils.DummyPoliticalData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class PromiseDetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableLiveData<PromiseDetailsState>()
    val uiState: LiveData<PromiseDetailsState> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.postValue(PromiseDetailsState.Error(throwable))
        Timber.e(throwable)
    }

    private lateinit var currentUser: User
    private lateinit var promiseId: String

    fun setPromiseId(id: String) {
        this.promiseId = id
    }

    fun getCurrentUserId() {
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                firebaseAuth.currentUser!!.uid
            }.onSuccess {
                getUser(userId = it)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }

    private fun getUser(userId: String) {
        _uiState.postValue(PromiseDetailsState.GetUserLoading)
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                firestore.collection(USERS_COLLECTION).document(userId).get().await()
                    .toObject(User::class.java)
            }.onSuccess {
                currentUser = it!!
                _uiState.postValue(PromiseDetailsState.GetUserSuccess)
                getComments(promiseId)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }

    fun addComment(comment: String) {
        _uiState.postValue(PromiseDetailsState.AddCommentsLoading)
        viewModelScope.launch(exceptionHandler) {
            val newComment = Comment(
                id = DummyPoliticalData.generateId(1..12),
                name = currentUser.fullName,
                userId = currentUser.userId,
                date = System.currentTimeMillis().toString(),
                comment = comment
            )
            kotlin.runCatching {
                firestore.collection(COMMENTS_COLLECTION.plus("/$COMMENTS_COLLECTION/$promiseId"))
                    .add(newComment)
                    .await()
            }.onSuccess {
                _uiState.postValue(PromiseDetailsState.AddCommentsSuccess)
                getComments(promiseId)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }

    private fun getComments(promiseId: String) {
        _uiState.postValue(PromiseDetailsState.GetCommentsLoading)
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                firestore.collection(COMMENTS_COLLECTION.plus("/$COMMENTS_COLLECTION/$promiseId"))
                    .get().await()
                    .toObjects(Comment::class.java)
            }.onSuccess {
                val comments = it.map { comment ->
                    comment.copy(
                        imageUrl = firestore.collection(USERS_COLLECTION)
                            .document(comment.userId).get()
                            .await()["profilePhoto"].toString()
                    )
                }
                _uiState.postValue(PromiseDetailsState.GetCommentsSuccess(comments))
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }
}

sealed class PromiseDetailsState {
    data class GetCommentsSuccess(val comments: List<Comment>) : PromiseDetailsState()
    object GetCommentsLoading : PromiseDetailsState()
    object AddCommentsLoading : PromiseDetailsState()
    object GetUserLoading : PromiseDetailsState()
    object AddCommentsSuccess : PromiseDetailsState()
    object GetUserSuccess : PromiseDetailsState()
    data class Error(val error: Throwable) : PromiseDetailsState()
}
