package com.sdn.teampredators.polima.ui.aspirant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class AspirantViewModel : ViewModel() {

    private val _uiState = MutableLiveData<AspirantTaskState>()
    val uiState: LiveData<AspirantTaskState> = _uiState

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    init{
        val taskContent = listOf(
            AspirantTaskItem(
                taskImage = R.drawable.ic_vote,
                taskHeader = R.string.vote,
                taskContent = R.string.long_text
            ),
            AspirantTaskItem(
                taskImage = R.drawable.ic_verified,
                taskHeader = R.string.check,
                taskContent = R.string.long_text
            ),
            AspirantTaskItem(
                taskImage = R.drawable.ic_person,
                taskHeader = R.string.view,
                taskContent = R.string.long_text
            )
        )

        _uiState.value = AspirantTaskState.Content(taskContent)

    }

    fun toVotePromises(promises: Politician) {
        _action.value = GenericActions.Navigate(AspirantFragmentDirections.toVoteFragment(promises))
    }

    fun toVerifyPromises(verifyPromises: Politician){
        _action.value = GenericActions.Navigate(AspirantFragmentDirections.toVerifyFragment(verifyPromises))
    }

    fun toAspirantProfile(profileDetails: Politician){
        _action.value = GenericActions.Navigate(AspirantFragmentDirections.toProfileFragment(profileDetails))
    }
}

sealed class AspirantTaskState {
    data class Content(val taskItem: List<AspirantTaskItem>) : AspirantTaskState()
}