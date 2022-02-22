package com.sdn.teampredators.polima.ui.aspirant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.AspirantTaskItemBinding

class AspirantAdapter :
    ListAdapter<AspirantItem, AspirantAdapter.AspirantTaskViewHolder>(DIFF_UTIL) {

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
    }

    inner class AspirantTaskViewHolder(private val binding: AspirantTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AspirantItem) = with(binding) {
            val context = root.context
            taskHeaderText.text = context.getText(item.taskHeader)
            taskContentText.text = context.getText(item.taskContent)
            taskImage.setImageResource(item.taskImage)
            taskCard.setOnClickListener { item.onClick.invoke() }
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
