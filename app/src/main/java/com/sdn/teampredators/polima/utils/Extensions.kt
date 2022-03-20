package com.sdn.teampredators.polima.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.utils.Constants.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Locale
import timber.log.Timber

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

fun Fragment.hideSoftKeyboard(view: View) {
    val imm: InputMethodManager =
        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Long.formatDate(): String {
    return try {
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        sdf.format(this)
    } catch (t: Throwable) {
        Timber.e(t)
        ""
    }
}
