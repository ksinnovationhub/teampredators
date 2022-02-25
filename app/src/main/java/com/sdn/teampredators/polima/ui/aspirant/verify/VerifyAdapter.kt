package com.sdn.teampredators.polima.ui.aspirant.verify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ItemVerifyBinding
import com.sdn.teampredators.polima.ui.home.model.Promise

class VerifyAdapter: ListAdapter<Promise, VerifyAdapter.VerifyViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerifyViewHolder {
        return VerifyViewHolder(
            ItemVerifyBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_verify, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: VerifyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VerifyViewHolder(private val binding: ItemVerifyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Promise) = with(binding) {
            verifyHeaderText.text= item.promise
            verifyPromisesText.text = item.promiseDescription
            verifyVoteNumbersUp.text = item.upVoteCount.toString()
            verifyVoteNumbersDown.text = item.downVoteCount.toString()
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Promise>() {
            override fun areItemsTheSame(
                oldItem: Promise,
                newItem: Promise
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Promise,
                newItem: Promise
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
