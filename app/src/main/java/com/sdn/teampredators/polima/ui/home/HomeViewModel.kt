package com.sdn.teampredators.polima.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.DummyPoliticalData
import com.sdn.teampredators.polima.utils.ListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {

    private val _aspirantData = MutableSharedFlow<ListResult<Politician>>()
    val aspirantData = _aspirantData.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    init {
        repeat(30) {
            //setData() // todo: uncomment to populate the database with random data
        }
    }

    private fun setData() {
        viewModelScope.launch(exceptionHandler) {
            kotlin.runCatching {
                val politician = DummyPoliticalData.getPolitician()
                db.collection("politicians").document(politician.id).set(politician).await()
            }
                .onSuccess { Timber.d("Successfully added dummy data") }
                .onFailure { Timber.e(it) }
        }
    }

    fun getData() {
        viewModelScope.launch(exceptionHandler) {
            _aspirantData.emit(ListResult.Loading)
            kotlin.runCatching {
                db.collection("politicians").get().await()
            }.fold(onSuccess = {
                if (!it.isEmpty) {
                    _aspirantData.emit(ListResult.Success(it.toObjects(Politician::class.java)))
                } else {
                    _aspirantData.emit(
                        ListResult.Error( // todo: This should be changed to an `empty` state instead of error state
                            error = "No data found in Database"
                        )
                    )
                }
            }, onFailure = {
                _aspirantData.emit(
                    ListResult.Error(
                        it.localizedMessage
                    )
                )
            })
        }
    }
}
