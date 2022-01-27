package com.sdn.teampredators.polima.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.AspirantItemLayoutBinding
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.load

class PolimaPoliticianAdapter(
    private val navigate: ToAspirantTask
) :
    ListAdapter<Politician, PolimaPoliticianAdapter.FirestoreViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirestoreViewHolder {
        return FirestoreViewHolder(
            AspirantItemLayoutBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.aspirant_item_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: PolimaPoliticianAdapter.FirestoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FirestoreViewHolder(private val binding: AspirantItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Politician) = with(binding) {
            aspirantName.text = item.fullName
            aspirantParty.text = item.party
            aspirantPosition.text = item.position
            aspirantImage.load(item.photoUrl)
            root.setOnClickListener {
                navigate.toAspirantTask(item)
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Politician>() {
            override fun areItemsTheSame(
                oldItem: Politician,
                newItem: Politician
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Politician,
                newItem: Politician
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface ToAspirantTask{
    fun toAspirantTask(item: Politician)
}
