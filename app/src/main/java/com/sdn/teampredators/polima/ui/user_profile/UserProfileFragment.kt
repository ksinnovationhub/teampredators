package com.sdn.teampredators.polima.ui.user_profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentUserProfileBinding
import com.sdn.teampredators.polima.ui.auth.model.User
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private val binding by viewBinding(FragmentUserProfileBinding::bind)
    private val viewModel by viewModels<UserProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage loadRoundImage R.drawable.profile_photo_placeholder
        setupObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.logoutButton.setOnClickListener {
            // TODO: Display dialog first
            viewModel.logout()
        }
    }

    private fun setupObservers() {
        viewModel.userResponse.observe(viewLifecycleOwner) {
            when(it) {
                ProfileState.Loading -> {}
                is ProfileState.Error -> {}
                is ProfileState.Success -> displayContent(it.user)
            }
        }
    }

    private fun displayContent(user: User?) = with(binding) {
        nameText.text = user?.fullName
        ninValue.text = user?.nin
        emailValue.text = user?.email
    }
}
