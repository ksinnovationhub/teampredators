package com.sdn.teampredators.polima.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingItem(
    @DrawableRes val onboardingIllustration: Int,
    @StringRes val onboardingTitle: Int,
    @StringRes val onboardingDescription: Int
)
