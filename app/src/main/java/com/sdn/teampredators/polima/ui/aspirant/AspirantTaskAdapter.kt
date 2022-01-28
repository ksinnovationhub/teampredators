package com.sdn.teampredators.polima.ui.aspirant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.AspirantTaskItemBinding

class AspirantTaskAdapter(
    val navigation: (AspirantDestinations) -> Unit
) : ListAdapter<AspirantItem, AspirantTaskAdapter.AspirantTaskViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AspirantTaskViewHolder {
        return AspirantTaskViewHolder(
            AspirantTaskItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.aspirant_task_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: AspirantTaskViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.cardView.setOnClickListener { holder.navigate(position) }
    }

    inner class AspirantTaskViewHolder(private val binding: AspirantTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.taskCard

        fun bind(item: AspirantItem) = with(binding) {
            val context = root.context
            taskHeaderText.text = context.getText(item.taskHeader)
            taskContentText.text = context.getText(item.taskContent)
            taskImage.setImageResource(item.taskImage)

        }

        fun navigate(position: Int) {
            when (position) {
                0 -> { navigation.invoke(AspirantDestinations.Vote) }
                1 -> { navigation.invoke(AspirantDestinations.Verify) }
                2 -> { navigation.invoke(AspirantDestinations.Profile) }
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AspirantItem>() {
            override fun areItemsTheSame(
                oldItem: AspirantItem,
                newItem: AspirantItem
            ): Boolean {
                return oldItem.taskImage == newItem.taskImage
            }

            override fun areContentsTheSame(
                oldItem: AspirantItem,
                newItem: AspirantItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

sealed class AspirantDestinations {
    object Vote : AspirantDestinations()
    object Verify : AspirantDestinations()
    object Profile : AspirantDestinations()
}
