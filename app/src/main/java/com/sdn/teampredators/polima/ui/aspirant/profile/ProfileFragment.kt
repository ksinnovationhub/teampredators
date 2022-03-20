package com.sdn.teampredators.polima.ui.aspirant.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentProfileBinding
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import kotlinx.coroutines.delay

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val navArgs by navArgs<ProfileFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fakeNetworkCall()
    }

    private fun fakeNetworkCall() = with(binding) {
        lifecycleScope.launchWhenStarted {
            delay(700)
            setUpProfile()
        }
        progressBar.root.apply {
            viewState(true)
            postDelayed({ this.viewState(false) }, 800)
        }
    }

    private fun setUpProfile() = with(binding) {
        with(navArgs.profileItem) {
            aspirantProfileImage loadRoundImage photoUrl
            aspirantProfileName.text = fullName
            aspirantProfilePosition.text = position
            aspirantProfileAge.text = getString(R.string.age).plus(age)
            profileItem.aspirantProfileBioContent.text = biography
            profileItem.aspirantProfileEduContent.text = education
            profileItem.aspirantProfilePaContent.text = politicalAspirations
        }
    }
}
