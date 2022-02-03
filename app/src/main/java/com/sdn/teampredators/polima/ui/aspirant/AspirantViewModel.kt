package com.sdn.teampredators.polima.ui.aspirant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class AspirantViewModel : ViewModel() {

    private val _uiState = MutableLiveData<AspirantState>()
    val uiState: LiveData<AspirantState> = _uiState

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    init {
        val taskContent = listOf(
            AspirantItem(
                taskImage = R.drawable.ic_vote,
                taskHeader = R.string.vote,
                taskContent = R.string.vote_description
            ),
            AspirantItem(
                taskImage = R.drawable.ic_verified,
                taskHeader = R.string.check,
                taskContent = R.string.check_verified_promises_description
            ),
            AspirantItem(
                taskImage = R.drawable.ic_person,
                taskHeader = R.string.view,
                taskContent = R.string.candidate_bio_description
            )
        )

        _uiState.value = AspirantState.Content(taskContent)

    }

    fun toVotePromises(promises: Politician) {
        _action.value = GenericActions.Navigate(AspirantFragmentDirections.toVoteFragment(promises))
    }

    fun toVerifyPromises(verifyPromises: Politician) {
        _action.value =
            GenericActions.Navigate(AspirantFragmentDirections.toVerifyFragment(verifyPromises))
    }

    fun toAspirantProfile(profileDetails: Politician) {
        _action.value =
            GenericActions.Navigate(AspirantFragmentDirections.toProfileFragment(profileDetails))
    }
}

sealed class AspirantState {
    data class Content(val item: List<AspirantItem>) : AspirantState()
}
