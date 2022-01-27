package com.sdn.teampredators.polima.ui.aspirant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.AspirantTaskItemBinding
import com.sdn.teampredators.polima.ui.home.model.Politician

class AspirantTaskAdapter(
    val politicianItem: Politician,
    val navigation: ToAspirantThreeScreens
) :
    ListAdapter<AspirantTaskItem, AspirantTaskAdapter.AspirantTaskViewHolder>(DIFF_UTIL) {

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

        fun bind(item: AspirantTaskItem) = with(binding) {
            val context = root.context
            taskHeaderText.text = context.getText(item.taskHeader)
            taskContentText.text = context.getText(item.taskContent)
            taskImage.setImageResource(item.taskImage)

        }

        fun navigate(position: Int) {
            when (position) {
                0 -> {
                    navigation.toVote(politicianItem)
                }
                1 -> {
                    navigation.toVerify(politicianItem)
                }
                2 -> {
                    navigation.toProfile(politicianItem)
                }
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AspirantTaskItem>() {
            override fun areItemsTheSame(
                oldItem: AspirantTaskItem,
                newItem: AspirantTaskItem
            ): Boolean {
                return oldItem.taskImage == newItem.taskImage
            }

            override fun areContentsTheSame(
                oldItem: AspirantTaskItem,
                newItem: AspirantTaskItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface ToAspirantThreeScreens {
    fun toVote(item: Politician)
    fun toVerify(item: Politician)
    fun toProfile(item: Politician)
}
