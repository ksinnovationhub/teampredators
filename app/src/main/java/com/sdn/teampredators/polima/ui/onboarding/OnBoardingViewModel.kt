package com.sdn.teampredators.polima.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class OnBoardingViewModel : ViewModel() {

    private val _uiState = MutableLiveData<OnBoardingState>()
    val uiState: LiveData<OnBoardingState> = _uiState

    private val _action = SingleLiveEvent<GenericActions>()
    val action: LiveData<GenericActions> = _action

    init {
        val contents = listOf(
            OnBoardingItem(
                onboardingIllustration = R.drawable.il_onboarding_1,
                onboardingTitle = R.string.onboarding_header_1,
                onboardingDescription = R.string.onboarding_description_1
            ),
            OnBoardingItem(
                onboardingIllustration = R.drawable.il_onboarding_2,
                onboardingTitle = R.string.onboarding_header_2,
                onboardingDescription = R.string.onboarding_description_2
            ),
            OnBoardingItem(
                onboardingIllustration = R.drawable.il_onboarding_3,
                onboardingTitle = R.string.onboarding_header_3,
                onboardingDescription = R.string.onboarding_description_3
            )
        )

        _uiState.value = OnBoardingState.Content(contents)
    }

    fun toLogin() {
        _action.value = GenericActions.Navigate(OnBoardingFragmentDirections.toSignInFragment())
    }
}

sealed class OnBoardingState {
    data class Content(val onboardingItems: List<OnBoardingItem>) : OnBoardingState()
}
