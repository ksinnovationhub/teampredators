package com.sdn.teampredators.polima.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ItemOnboardingBinding

class OnBoardingAdapter :
    ListAdapter<OnBoardingItem, OnBoardingAdapter.OnBoardingViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            ItemOnboardingBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OnBoardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnBoardingItem) = with(binding) {
            val context = root.context
            onboardingTitleText.text = context.getText(item.onboardingTitle)
            onboardingDescriptionText.text = context.getText(item.onboardingDescription)
            illustrationView.setImageResource(item.onboardingIllustration)
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<OnBoardingItem>() {
            override fun areItemsTheSame(
                oldItem: OnBoardingItem,
                newItem: OnBoardingItem
            ): Boolean {
                return oldItem.onboardingIllustration == newItem.onboardingIllustration
            }

            override fun areContentsTheSame(
                oldItem: OnBoardingItem,
                newItem: OnBoardingItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
