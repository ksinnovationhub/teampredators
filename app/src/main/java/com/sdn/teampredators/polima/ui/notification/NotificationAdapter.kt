package com.sdn.teampredators.polima.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.NotificationItemLayoutBinding
import com.sdn.teampredators.polima.utils.viewState

class NotificationAdapter(
    private val onItemClick: (NotificationModel) -> Unit
) : ListAdapter<NotificationModel, NotificationAdapter.NotificationViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationItemLayoutBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.notification_item_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(private val binding: NotificationItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationItem: NotificationModel) = with(binding) {
            val context = root.context
            if (!notificationItem.isRead) {
                notificationText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.notification_color
                    )
                )
                notificationDate.setTextColor(ContextCompat.getColor(context, R.color.black))
                notificationIndicator.viewState(true)
            } else {
                notificationText.setTextColor(ContextCompat.getColor(context, R.color.dullBlack))
                notificationDate.setTextColor(ContextCompat.getColor(context, R.color.dullBlack))
                notificationIndicator.viewState(false)
            }

            notificationImage.setImageResource(notificationItem.notificationImage)
            notificationText.text = notificationItem.title
            notificationDate.text = notificationItem.days

            root.setOnClickListener {
                onItemClick.invoke(notificationItem)
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<NotificationModel>() {
            override fun areItemsTheSame(
                oldItem: NotificationModel,
                newItem: NotificationModel
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: NotificationModel,
                newItem: NotificationModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class NotificationModel(
    @DrawableRes val notificationImage: Int,
    val title: String,
    val days: String,
    val isRead: Boolean
)
