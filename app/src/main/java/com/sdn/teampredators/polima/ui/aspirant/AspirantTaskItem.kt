package com.sdn.teampredators.polima.ui.aspirant

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AspirantTaskItem(
    @DrawableRes val taskImage: Int,
    @StringRes val taskHeader: Int,
    @StringRes val taskContent: Int
)