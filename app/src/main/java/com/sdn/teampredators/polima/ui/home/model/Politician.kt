package com.sdn.teampredators.polima.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Politician(
    val id: String,
    val fullName: String,
    val party: String,
    val position: String,
    val photoUrl: String,
    val age: String,
    val biography: String,
    val politicalAspirations: String,
    val education: String,
    val promises: List<Promise>
): Parcelable {
    constructor() : this(
        id = "",
        fullName = "",
        party = "",
        position = "",
        photoUrl = "",
        age = "",
        biography = "",
        politicalAspirations = "",
        education = "",
        promises = emptyList()
    )
}
