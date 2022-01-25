package com.sdn.teampredators.polima.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.data.models.AspirantDto
import com.sdn.teampredators.polima.databinding.AspirantItemLayoutBinding
import com.sdn.teampredators.polima.utils.load


class PolimaFirestoreAdapter :
    ListAdapter<AspirantDto, PolimaFirestoreAdapter.FirestoreViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirestoreViewHolder {
        return FirestoreViewHolder(
            AspirantItemLayoutBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.aspirant_item_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: PolimaFirestoreAdapter.FirestoreViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    inner class FirestoreViewHolder(private val binding: AspirantItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AspirantDto) = with(binding) {
            aspirantName.text = item.name
            aspirantParty.text = item.party
            aspirantPosition.text = item.position
            aspirantImage.load(item.image_url)
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AspirantDto>() {
            override fun areItemsTheSame(
                oldItem: AspirantDto,
                newItem: AspirantDto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AspirantDto,
                newItem: AspirantDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}