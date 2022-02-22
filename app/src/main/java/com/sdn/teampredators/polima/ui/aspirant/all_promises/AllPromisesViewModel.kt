package com.sdn.teampredators.polima.ui.aspirant.all_promises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.ui.home.model.PromiseStatus

class AllPromisesViewModel(private val politician: Politician) : ViewModel() {

    private val _uiState = MutableLiveData<AllPromisesState>()
    val uiState: LiveData<AllPromisesState> = _uiState

    init {
        _uiState.value = AllPromisesState.Content(promises = politician.promises)
    }

    fun filter(status: PromiseStatus) {
        when (status) {
            PromiseStatus.COMPLETED, PromiseStatus.ONGOING, PromiseStatus.NOT_STARTED, PromiseStatus.ABANDONED -> {
                _uiState.postValue(AllPromisesState.Content(promises = politician.promises.filter {
                    it.status.equals(status.value, ignoreCase = true)
                }))
            }
            PromiseStatus.ALL -> {
                _uiState.postValue(AllPromisesState.Content(promises = politician.promises))
            }
        }
    }
}

sealed class AllPromisesState {
    data class Content(val promises: List<Promise>) : AllPromisesState()
}
