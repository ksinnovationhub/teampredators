package com.sdn.teampredators.polima.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promise(
    val id: String,
    val promise: String,
    val promiseDescription: String,
    val upVoteCount: Long = 0,
    val downVoteCount: Long = 0,
    val userIds: MutableList<String> = mutableListOf(),
    val status: String,
    val averageRating: Float = 0.0F,
    val totalNumberOfRating: Long = 0L,
    val promiseImages: MutableList<String> = mutableListOf()
) : Parcelable {
    constructor() : this(id = "", promise = "", promiseDescription = "", status = "")
}

enum class PromiseStatus(val value: String) {
    COMPLETED(value = "Completed"),
    NOT_STARTED(value = "Not Started"),
    ONGOING(value = "Ongoing"),
    ABANDONED(value = "Abandoned"),
    ALL(value = "All")
}
