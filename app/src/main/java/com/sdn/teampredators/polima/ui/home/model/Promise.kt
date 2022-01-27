package com.sdn.teampredators.polima.ui.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Promise(
    val id: String,
    val promise: String,
    val promiseDescription: String,
    val upVoteCount: Long = 0,
    val downVoteCount: Long = 0,
    val userIds: List<String> = emptyList()
): Parcelable {
    constructor(): this(id = "", promise = "", promiseDescription = "")
}
