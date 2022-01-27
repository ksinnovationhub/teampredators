package com.sdn.teampredators.polima.ui.aspirant.vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentVoteBinding
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import kotlinx.coroutines.delay

class VoteFragment : Fragment(R.layout.fragment_vote) {

    private val binding by viewBinding(FragmentVoteBinding::bind)
    private val viewModel by viewModels<VoteViewModel>()
    private val navArgs by navArgs<VoteFragmentArgs>()
    private val voteAdapter : VoteAdapter by lazy{ VoteAdapter() }

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
        with(navArgs.promisesItem) {
            aspirantImageVote.load(this.photoUrl)
            aspirantNameVote.text = this.fullName
            aspirantPositionVote.text = this.position
            aspirantPartyVote.text = this.party
        }
    }

    private fun setUpAdapter() = with(binding){
        voteRecyclerView.adapter = voteAdapter
        voteAdapter.submitList(navArgs.promisesItem.promises)
    }

}