package com.sdn.teampredators.polima.ui.aspirant.vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentVoteBinding
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.showMessage
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteFragment : Fragment(R.layout.fragment_vote) {

    private val binding by viewBinding(FragmentVoteBinding::bind)
    private val viewModel by viewModels<VoteViewModel>()
    private val args by navArgs<VoteFragmentArgs>()
    private val voteAdapter: VoteAdapter by lazy {
        VoteAdapter(
            onVoteUp = { viewModel.voteUp(it, args.politicianItem.id) },
            onVoteDown = { viewModel.voteDown(it, args.politicianItem.id) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setUpProfile()
        setUpAdapter()
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                VoteStates.Loading -> handleLoading()
                VoteStates.Success.VoteSuccess -> handleSuccessfulVoting()
                is VoteStates.Success.EmptyPromises -> handleEmptyState()
                is VoteStates.Success.NonEmptyPromises -> handleContent(state.data)
                is VoteStates.Error -> handleError(state.error)
            }
        }
    }

    private fun setUpProfile() = with(binding) {
        with(args.politicianItem) {
            aspirantImageVote.load(photoUrl)
            aspirantNameVote.text = fullName
            aspirantPositionVote.text = position
            aspirantPartyVote.text = party
        }
        viewModel.getPromises(args.politicianItem.id)
    }

    private fun setUpAdapter() = with(binding) {
        voteRecyclerView.adapter = voteAdapter
    }

    private fun handleContent(promises: List<Promise>) {
        binding.progressBar.root.viewState(state = false)
        binding.voteRecyclerView.viewState(state = true)
        voteAdapter.submitList(promises)
    }

    private fun handleLoading() {
        binding.progressBar.root.viewState(state = true)
    }

    private fun handleEmptyState() {
        binding.progressBar.root.viewState(state = false)
        binding.voteRecyclerView.viewState(state = false)
        // TODO: Update UI to reflect empty state
    }

    private fun handleSuccessfulVoting() {
        viewModel.getPromises(args.politicianItem.id)
    }

    private fun handleError(error: Throwable) {
        binding.progressBar.root.viewState(state = false)
        binding.root.showMessage(error.localizedMessage)
    }
}
