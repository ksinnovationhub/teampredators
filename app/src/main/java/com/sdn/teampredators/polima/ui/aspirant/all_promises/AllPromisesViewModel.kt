package com.sdn.teampredators.polima.ui.aspirant.all_promises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.ui.home.model.PromiseStatus
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class AllPromisesViewModel (private val politician: Politician) : ViewModel() {

    private val _uiState = MutableLiveData<AllPromisesState>()
    val uiState: LiveData<AllPromisesState> = _uiState

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

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

    fun toPromiseDetails(promiseId: String) {
        _action.value = GenericActions.Navigate(
            AllPromisesFragmentDirections.toPromiseDetailsFragment(
                politicianName = politician.fullName,
                politicianImage = politician.photoUrl,
                politicianId = politician.id,
                promiseId = promiseId
            )
        )
    }
}

sealed class AllPromisesState {
    data class Content(val promises: List<Promise>) : AllPromisesState()
}
