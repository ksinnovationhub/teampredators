package com.sdn.teampredators.polima.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.sdn.teampredators.polima.data.models.AspirantDto
import com.sdn.teampredators.polima.utils.ListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _aspirantData = MutableSharedFlow<ListResult<AspirantDto>>()
    val aspirantData = _aspirantData.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    fun getData() {
        viewModelScope.launch(exceptionHandler) {
            _aspirantData.emit(ListResult.Loading)
            kotlin.runCatching {
                db.collection("politicians").get().await()
            }.fold(onSuccess = {
                if (!it.isEmpty) {
                    _aspirantData.emit(ListResult.Success(it.toObjects(AspirantDto::class.java)))
                } else {
                    _aspirantData.emit(
                        ListResult.Error(
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