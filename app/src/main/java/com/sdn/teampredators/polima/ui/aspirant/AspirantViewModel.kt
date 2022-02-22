package com.sdn.teampredators.polima.ui.aspirant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.ui.aspirant.all_promises.AllPromisesViewModel
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class AspirantViewModel(private val promises: Politician) : ViewModel() {

    private val _uiState = MutableLiveData<AspirantState>()
    val uiState: LiveData<AspirantState> = _uiState

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    init {
        val taskContent = listOf(
            AspirantItem(
                taskImage = R.drawable.ic_scorecard,
                taskHeader = R.string.view_scorecard,
                taskContent = R.string.score_card_description,
                onClick = {
                    _action.value =
                        GenericActions.Navigate(AspirantFragmentDirections.toVoteFragment(promises))
                }
            ),
            AspirantItem(
                taskImage = R.drawable.ic_vote,
                taskHeader = R.string.view_promises,
                taskContent = R.string.promises_description,
                onClick = {
                    _action.value =
                        GenericActions.Navigate(
                            AspirantFragmentDirections.toAllPromisesFragment(promises)
                        )
                }
            ),
            AspirantItem(
                taskImage = R.drawable.ic_person,
                taskHeader = R.string.view,
                taskContent = R.string.candidate_bio_description,
                onClick = {
                    _action.value =
                        GenericActions.Navigate(
                            AspirantFragmentDirections.toProfileFragment(promises)
                        )
                }
            )
        )

        _uiState.value = AspirantState.Content(taskContent)
    }
}

sealed class AspirantState {
    data class Content(val item: List<AspirantItem>) : AspirantState()
}

@Suppress("UNCHECKED_CAST")
class AspirantViewModelFactory(private val promises: Politician) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AspirantViewModel::class.java) -> {
                AspirantViewModel(promises) as T
            }
            modelClass.isAssignableFrom(AllPromisesViewModel::class.java) -> {
                AllPromisesViewModel(promises) as T
            }
            else -> {
                throw IllegalArgumentException("Class ${modelClass.canonicalName} cannot be found")
            }
        }
    }
}
