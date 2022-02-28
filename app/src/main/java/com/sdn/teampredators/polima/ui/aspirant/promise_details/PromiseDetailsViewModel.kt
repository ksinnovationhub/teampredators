package com.sdn.teampredators.polima.ui.aspirant.promise_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.aspirant.promise_details.model.Comment
import com.sdn.teampredators.polima.ui.auth.model.User
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.utils.Constants.COMMENTS_COLLECTION
import com.sdn.teampredators.polima.utils.Constants.POLITICIAN_COLLECTION
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
    private var promises: MutableList<Promise> = mutableListOf()
    private lateinit var politicianId: String
    private lateinit var promiseId: String

    fun setData(politicianId: String, promiseId: String) {
        this.politicianId = politicianId
        this.promiseId = promiseId
    }

    fun getCurrentUserId() {
        _uiState.postValue(PromiseDetailsState.LoadUiContentsLoading)
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
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                firestore.collection(USERS_COLLECTION).document(userId).get().await()
                    .toObject(User::class.java)
            }.onSuccess {
                currentUser = it!!
                loadUiContent(this@PromiseDetailsViewModel.politicianId)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }

    private suspend fun getPromisesUpdated(politicianId: String): Result<List<Promise>?> {
        return kotlin.runCatching {
            firestore.collection(POLITICIAN_COLLECTION)
                .document(politicianId)
                .get()
                .await()
                .toObject(Politician::class.java)
                ?.promises
        }.onFailure { _uiState.postValue(PromiseDetailsState.Error(it)) }
    }

    private suspend fun getCommentsUpdated(): Result<List<Comment>> {
        return kotlin.runCatching {
            firestore.collection(COMMENTS_COLLECTION.plus("/$COMMENTS_COLLECTION/$promiseId"))
                .get().await()
                .toObjects(Comment::class.java).map { comment ->
                    comment.copy(
                        imageUrl = firestore.collection(USERS_COLLECTION)
                            .document(comment.userId).get()
                            .await()["profilePhoto"].toString()
                    )
                }
        }.onFailure { _uiState.postValue(PromiseDetailsState.Error(it)) }
    }

    private fun loadUiContent(politicianId: String) {
        viewModelScope.launch(exceptionHandler) {
            val comments = getCommentsUpdated().getOrNull()
            val promises = getPromisesUpdated(politicianId).getOrNull()
            comments?.let { safeComments ->
                promises?.let { safePromises ->
                    this@PromiseDetailsViewModel.promises.addAll(safePromises)
                    _uiState.postValue(
                        PromiseDetailsState.LoadUiContentsSuccess(
                            promises = safePromises,
                            comments = safeComments.asReversed()
                        )
                    )
                }
            }
        }
    }

    fun addComment(comment: String) {
        _uiState.postValue(PromiseDetailsState.AddCommentsLoading)
        viewModelScope.launch(exceptionHandler) {
            val tempPromise = promises.find {
                it.id == promiseId
            }
            val position = promises.indexOf(tempPromise)
            val newComment = Comment(
                id = DummyPoliticalData.generateId(1..12),
                name = currentUser.fullName,
                userId = currentUser.userId,
                date = System.currentTimeMillis().toString(),
                comment = comment
            )
            kotlin.runCatching {
                firestore.collection(COMMENTS_COLLECTION.plus("/$COMMENTS_COLLECTION/${promises[position].id}"))
                    .add(newComment)
                    .await()
            }.onSuccess {
                loadUiContent(this@PromiseDetailsViewModel.politicianId)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }

    fun rateProject(rating: Int) {
        _uiState.postValue(PromiseDetailsState.RatePromiseLoading)
        viewModelScope.launch(exceptionHandler) {
            val tempPromise = promises.find {
                it.id == promiseId
            }
            val position = promises.indexOf(tempPromise)
            val promise = promises[position]
            val currentUserIds = promise.userIds
            currentUserIds.add(firebaseAuth.uid!!)
            val totalNumberOfRating = promise.totalNumberOfRating + 1
            val promiseCopy = promise.copy(
                userIds = currentUserIds,
                totalNumberOfRating = totalNumberOfRating,
                averageRating = if (promise.totalNumberOfRating == 0L) rating.toFloat()
                else
                    ((promise.averageRating.times(promise.totalNumberOfRating)).plus(rating.toFloat()))
                        .div(totalNumberOfRating)
            )
            promises[position] = promiseCopy
            Timber.d("$promises")
            kotlin.runCatching {
                firestore.collection(POLITICIAN_COLLECTION).document(politicianId)
                    .update("promises", this@PromiseDetailsViewModel.promises)
            }.onSuccess {
                loadUiContent(this@PromiseDetailsViewModel.politicianId)
            }.onFailure {
                _uiState.postValue(PromiseDetailsState.Error(it))
            }
        }
    }
}

sealed class PromiseDetailsState {
    data class LoadUiContentsSuccess(val promises: List<Promise>, val comments: List<Comment>) :
        PromiseDetailsState()
    object LoadUiContentsLoading : PromiseDetailsState()
    object AddCommentsLoading : PromiseDetailsState()
    object RatePromiseLoading : PromiseDetailsState()
    data class Error(val error: Throwable) : PromiseDetailsState()
}
