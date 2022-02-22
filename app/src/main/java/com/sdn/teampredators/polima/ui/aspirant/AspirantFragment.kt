package com.sdn.teampredators.polima.ui.aspirant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentAspirantBinding
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import kotlinx.coroutines.delay

class AspirantFragment : Fragment(R.layout.fragment_aspirant) {

    private val binding by viewBinding(FragmentAspirantBinding::bind)
    private val args by navArgs<AspirantFragmentArgs>()
    private val viewModel by viewModels<AspirantViewModel> {
        AspirantViewModelFactory(args.politicianItem)
    }

    private val adapter: AspirantAdapter =
        AspirantAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //for fake network call
        fakeNetworkCall()
        setupObservers()
    }

    private fun fakeNetworkCall() = with(binding) {
        lifecycleScope.launchWhenStarted {
            delay(700)
            setUpTaskProfile()
            setUpAdapter()
        }
        progressBar.root.apply {
            viewState(true)
            postDelayed({ this.viewState(false) }, 800)
        }
    }

    private fun setUpAdapter() = with(binding) {
        taskRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AspirantState.Content -> renderContent(it.item)
            }
        }
        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is GenericActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun renderContent(item: List<AspirantItem>) {
        adapter.submitList(item)
    }

    private fun setUpTaskProfile() = with(binding) {
        with(args.politicianItem) {
            aspirantNameFragment.text = fullName
            aspirantPositionFragment.text = position
            aspirantPartyFragment.text = party
            aspirantImageFragment.load(photoUrl)
        }
    }
}
