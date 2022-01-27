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
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.GenericActions
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import kotlinx.coroutines.delay

class AspirantFragment : Fragment(R.layout.fragment_aspirant), ToAspirantThreeScreens {

    private val binding by viewBinding(FragmentAspirantBinding::bind)
    private val viewModel by viewModels<AspirantViewModel>()
    private val navArgs by navArgs<AspirantFragmentArgs>()
    private val taskAdapter: AspirantTaskAdapter by lazy {
        AspirantTaskAdapter(
            navArgs.politicianItem,
            this
        )
    }

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
        taskRecyclerView.adapter = taskAdapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AspirantTaskState.Content -> renderContent(it.taskItem)
            }
        }
        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is GenericActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun renderContent(taskItem: List<AspirantTaskItem>) {
        taskAdapter.submitList(taskItem)
    }

    private fun setUpTaskProfile() = with(binding) {
        with(navArgs.politicianItem) {
            aspirantNameFragment.text = this.fullName
            aspirantPositionFragment.text = this.position
            aspirantPartyFragment.text = this.party
            aspirantImageFragment.load(this.photoUrl)
        }
    }

    override fun toVote(item: Politician) {
        viewModel.toVotePromises(item)
    }

    override fun toVerify(item: Politician) {
        viewModel.toVerifyPromises(item)
    }

    override fun toProfile(item: Politician) {
        viewModel.toAspirantProfile(item)
    }

}