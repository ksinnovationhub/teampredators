package com.sdn.teampredators.polima.ui.aspirant.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ItemVoteBinding
import com.sdn.teampredators.polima.ui.home.model.Promise

class VoteAdapter(
    private val onVoteUp: (String) -> Unit,
    private val onVoteDown: (String) -> Unit
) : ListAdapter<Promise, VoteAdapter.VoteViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        return VoteViewHolder(
            ItemVoteBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_vote, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VoteViewHolder(private val binding: ItemVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Promise) = with(binding) {
            voteHeaderText.text = item.promise
            votePromisesText.text = item.promiseDescription
            voteUp.setOnClickListener { onVoteUp.invoke(item.id) }
            voteDown.setOnClickListener { onVoteDown.invoke(item.id) }
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
