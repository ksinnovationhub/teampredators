package com.sdn.teampredators.polima.ui.home.model

data class Promise(
    val id: String,
    val promise: String,
    val promiseDescription: String,
    val upVoteCount: Long = 0,
    val downVoteCount: Long = 0,
    val userIds: List<String> = emptyList()
) {
    constructor(): this(id = "", promise = "", promiseDescription = "")
}
