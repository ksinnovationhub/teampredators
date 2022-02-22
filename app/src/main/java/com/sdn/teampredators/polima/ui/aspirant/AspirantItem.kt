package com.sdn.teampredators.polima.ui.aspirant

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AspirantItem(
    @DrawableRes val taskImage: Int,
    @StringRes val taskHeader: Int,
    @StringRes val taskContent: Int,
    val onClick: () -> Unit
)
