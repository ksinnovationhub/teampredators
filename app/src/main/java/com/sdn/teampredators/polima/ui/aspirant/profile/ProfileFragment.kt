package com.sdn.teampredators.polima.ui.aspirant.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentProfileBinding
import com.sdn.teampredators.polima.utils.load
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

    private fun setUpProfile() = with(binding){
        with(navArgs.profileItem) {
            aspirantProfileImage.load(this.photoUrl)
            aspirantProfileName.text = this.fullName
            aspirantProfilePosition.text = this.position
            aspirantProfileAge.text = getString(R.string.age).plus(this.age)
            profileItem.aspirantProfileBioContent.text = this.biography
            profileItem.aspirantProfileEduContent.text = this.education
            profileItem.aspirantProfilePaContent.text = this.politicalAspirations
        }
    }

}