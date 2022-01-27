package com.sdn.teampredators.polima.ui.aspirant.verify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentVerifyBinding
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import kotlinx.coroutines.delay

class VerifyFragment : Fragment(R.layout.fragment_verify) {

    private val binding by viewBinding(FragmentVerifyBinding::bind)
    private val viewModel by viewModels<VerifyViewModel>()
    private val navArgs by navArgs<VerifyFragmentArgs>()
    private val verifyAdapter : VerifyAdapter by lazy{ VerifyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fakeNetworkCall()
    }

    private fun fakeNetworkCall() = with(binding) {
        lifecycleScope.launchWhenStarted {
            delay(700)
            setUpProfile()
            setUpAdapter()
        }
        progressBar.root.apply {
            viewState(true)
            postDelayed({ this.viewState(false) }, 800)
        }
    }

    private fun setUpProfile() = with(binding){
        with(navArgs.verifiedItem) {
            aspirantImageVerify.load(this.photoUrl)
            aspirantNameVerify.text = this.fullName
            aspirantPositionVerify.text = this.position
            aspirantPartyVerify.text = this.party
        }
    }

    private fun setUpAdapter() = with(binding){
        verifyRecyclerView.adapter = verifyAdapter
        verifyAdapter.submitList(navArgs.verifiedItem.promises)
    }

}