package com.sdn.teampredators.polima.ui.aspirant.all_promises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ItemProjectsBinding
import com.sdn.teampredators.polima.ui.home.model.Promise

class AllPromisesAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<Promise, AllPromisesAdapter.AllPromisesViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPromisesViewHolder {
        return AllPromisesViewHolder(
            ItemProjectsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_projects, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: AllPromisesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AllPromisesViewHolder(private val binding: ItemProjectsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promise: Promise) = with(binding) {
            projectTitle.text = promise.promise
            projectDescription.text = promise.promiseDescription
            projectStatus.text = root.context.getString(R.string.project_status, promise.status)
            projectSeeMore.setOnClickListener { onClick.invoke(promise.id) }
            root.setOnClickListener { onClick.invoke(promise.id) }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Promise>() {
            override fun areItemsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return oldItem == newItem
            }
        }
    }
}
