package com.sdn.teampredators.polima.utils

import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.ui.notification.NotificationModel
import kotlin.random.Random

object DummyNotificationData {

    private val notificationText = listOf(
        "Wike’s promise has just been verified by more than 200 people on polima. Join if you ....",
        "Buhari’s promise has just been verified by more than 2000 people on polima. Join if you ...."
    )

    private val notificationDays = listOf(
        "2 days ago",
        "3 days ago",
        "4 days ago",
        "5 days ago",
        "6 days ago",
        "1 month ago",
        "2 months ago"
    )

    private val notificationImage = listOf(
        R.drawable.ic_group,
        R.drawable.ic_group
    )

    private val readNotification = listOf(
        true,
        false
    )

    private fun getNotificationsTitle(): String =
        notificationText[Random.nextInt(notificationText.size)]

    private fun getNotificationDays(): String =
        notificationDays[Random.nextInt(notificationDays.size)]

    private fun getNotificationImages(): Int =
        notificationImage[Random.nextInt(notificationImage.size)]

    private fun getReadNotification(): Boolean =
        readNotification[Random.nextInt(readNotification.size)]

    fun getNotification(): NotificationModel {
        return NotificationModel(
            notificationImage = getNotificationImages(),
            title = getNotificationsTitle(),
            days = getNotificationDays(),
            isRead = getReadNotification()
        )
    }
}
