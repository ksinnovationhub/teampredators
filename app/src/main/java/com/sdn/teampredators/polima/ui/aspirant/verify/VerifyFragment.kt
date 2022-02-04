package com.sdn.teampredators.polima.ui.aspirant.verify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentVerifyBinding
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.showMessage
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyFragment : Fragment(R.layout.fragment_verify) {

    private val binding by viewBinding(FragmentVerifyBinding::bind)
    private val viewModel by viewModels<VerifyViewModel>()
    private val args by navArgs<VerifyFragmentArgs>()
    private val verifyAdapter: VerifyAdapter by lazy { VerifyAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProfile()
        setUpAdapter()
        setupObservers()
        viewModel.getPromises(politicianId = args.verifiedItem.id)
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                VerifyPromiseStates.Loading -> handleLoading()
                is VerifyPromiseStates.Error -> handleError(it.error)
                is VerifyPromiseStates.Success.EmptyPromises -> handleEmptyState()
                is VerifyPromiseStates.Success.NonEmptyPromises -> handleContent(it.data)
            }
        }
    }

    private fun setUpProfile() = with(binding) {
        with(args.verifiedItem) {
            aspirantImageVerify.load(photoUrl)
            aspirantNameVerify.text = fullName
            aspirantPositionVerify.text = position
            aspirantPartyVerify.text = party
        }
    }

    private fun setUpAdapter() = with(binding) {
        verifyRecyclerView.adapter = verifyAdapter
    }

    private fun handleContent(promises: List<Promise>) {
        binding.progressBar.root.viewState(state = false)
        verifyAdapter.submitList(promises)
    }

    private fun handleLoading() {
        binding.progressBar.root.viewState(state = true)
    }

    private fun handleEmptyState() {
        binding.progressBar.root.viewState(state = false)
        // TODO: Update UI to reflect empty state
    }

    private fun handleError(error: Throwable) {
        binding.progressBar.root.viewState(state = false)
        binding.root.showMessage(error.localizedMessage)
    }
}
