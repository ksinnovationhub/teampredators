package com.sdn.teampredators.polima.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentOnboardingBinding
import com.sdn.teampredators.polima.utils.Constants.ONBOARDING_PREF_KEY
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewModel by viewModels<OnBoardingViewModel>()
    private val onBoardingAdapter by lazy { OnBoardingAdapter() }
    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferences.getBoolean(ONBOARDING_PREF_KEY, false)) {
            viewModel.toLogin()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupPagerAdapter()
        setupClickListeners()
    }

    private fun setupClickListeners() = with(binding) {
        getStartedButton.setOnClickListener {
            viewModel.toLogin()
            preferences.edit().putBoolean(ONBOARDING_PREF_KEY, true).apply()
        }
        skipButton.setOnClickListener {
            viewModel.toLogin()
            preferences.edit().putBoolean(ONBOARDING_PREF_KEY, true).apply()
        }
        nextButton.setOnClickListener {
            onboardingPager.currentItem = onboardingPager.currentItem.plus(1)
        }
    }

    private fun setupPagerAdapter() = with(binding) {
        onboardingPager.adapter = onBoardingAdapter
        onboardingPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        indicator1.isSelected = true
                        indicator2.isSelected = false
                        indicator3.isSelected = false
                        scrollButtonGroup.isVisible = true
                        getStartedButton.isGone = true
                    }
                    1 -> {
                        indicator1.isSelected = false
                        indicator2.isSelected = true
                        indicator3.isSelected = false
                        scrollButtonGroup.isVisible = true
                        getStartedButton.isGone = true
                    }
                    2 -> {
                        indicator1.isSelected = false
                        indicator2.isSelected = false
                        indicator3.isSelected = true
                        scrollButtonGroup.isGone = true
                        getStartedButton.isVisible = true
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is OnBoardingState.Content -> renderContent(it.onboardingItems)
            }
        }
        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is OnBoardingAction.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun renderContent(items: List<OnBoardingItem>) {
        onBoardingAdapter.submitList(items)
    }
}
