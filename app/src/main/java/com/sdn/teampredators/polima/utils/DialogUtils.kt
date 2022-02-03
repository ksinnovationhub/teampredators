package com.sdn.teampredators.polima.utils

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.DialogShowNotificationBinding
import com.sdn.teampredators.polima.ui.notification.NotificationModel

fun Fragment.showNotificationDialog(notification: NotificationModel) {
    val binding = DialogShowNotificationBinding.bind(
        LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_show_notification, null, false)
    )
    with(binding) {
        val dialog = AlertDialog.Builder(root.context)
            .setView(root).create()
        dialog.setCancelable(true)

        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(root.context, android.R.color.transparent)
            )
        )

        okBtn.setOnClickListener { dialog.dismiss() }
        notificationTitle.text = notification.title
        notificationTime.text = notification.days
        dialog.show()
    }
}
