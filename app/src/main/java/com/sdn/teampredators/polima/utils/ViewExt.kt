package com.sdn.teampredators.polima.utils

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sdn.teampredators.polima.R

infix fun ImageView.load(file: String?) {
    Glide.with(this)
        .applyDefaultRequestOptions(requestManger())
        .load(file)
        .into(this)
}

infix fun ImageView.loadRoundImage(imageUrl: Any?) {
    Glide.with(this)
        .applyDefaultRequestOptions(requestManger())
        .load(imageUrl)
        .circleCrop()
        .into(this)
}

private fun requestManger() =
    RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_error_image)
        .centerCrop()


fun View.viewState(state: Boolean) {
    this.isVisible = state
}

fun View.showMessage(msg: String?) {
    msg?.let { Snackbar.make(this, it, Snackbar.LENGTH_LONG).show() }
}
