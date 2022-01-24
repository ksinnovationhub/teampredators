package com.sdn.teampredators.polima.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.utils.SingleLiveEvent

class OnBoardingViewModel : ViewModel() {

    private val _uiState = MutableLiveData<OnBoardingState>()
    val uiState: LiveData<OnBoardingState> = _uiState

    private val _action = SingleLiveEvent<OnBoardingAction>()
    val action: LiveData<OnBoardingAction> = _action

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
        _action.value = OnBoardingAction.Navigate(OnBoardingFragmentDirections.toSignInFragment())
    }
}

sealed class OnBoardingState {
    data class Content(val onboardingItems: List<OnBoardingItem>) : OnBoardingState()
}

sealed class OnBoardingAction {
    data class Navigate(val destination: NavDirections): OnBoardingAction()
}
