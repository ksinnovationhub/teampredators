package com.sdn.teampredators.polima.ui.aspirant.promise_details.model

data class Comment(
    val id: String,
    val userId: String,
    val name: String,
    val imageUrl: String? = null,
    val comment: String,
    val date: String
) {
    constructor() : this("", "", "", "", "", "")
}
