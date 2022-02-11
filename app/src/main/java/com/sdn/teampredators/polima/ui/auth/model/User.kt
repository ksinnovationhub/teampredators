package com.sdn.teampredators.polima.ui.auth.model

data class User(
    val userId: String,
    val email: String,
    val fullName: String,
    val nin: String,
    val gender: String,
    val profilePhoto: String = "",
    val promises: List<String> = emptyList()
) {
    constructor() : this("", "", "", "", "")
}
