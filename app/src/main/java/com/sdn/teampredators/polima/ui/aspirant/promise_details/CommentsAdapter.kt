package com.sdn.teampredators.polima.ui.aspirant.promise_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ItemCommentBinding
import com.sdn.teampredators.polima.ui.aspirant.promise_details.model.Comment
import com.sdn.teampredators.polima.utils.formatDate
import com.sdn.teampredators.polima.utils.loadRoundImage

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.CommentsViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            ItemCommentBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_comment, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentsViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            userName.text = comment.name
            userComment.text = comment.comment
            commentDate.text = comment.date.toLong().formatDate()
            profilePhoto loadRoundImage comment.imageUrl
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}
