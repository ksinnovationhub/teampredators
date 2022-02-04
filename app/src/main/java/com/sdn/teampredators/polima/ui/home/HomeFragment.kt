package com.sdn.teampredators.polima.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentHomeBinding
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val politicianAdapter: PolimaPoliticianAdapter =
        PolimaPoliticianAdapter(navigate = { item ->
            viewModel.toAspirantTask(item)
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.aspirantData.observe(viewLifecycleOwner) {
            when (it) {
                HomeStates.Loading -> loading()
                HomeStates.Empty -> handleEmptyState()
                is HomeStates.NonEmpty -> success(it.content)
                is HomeStates.Error -> error(it.error.localizedMessage)
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is GenericActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
        binding.include.searchEditText.addTextChangedListener {
            viewModel.searchPolitician(it.toString())
        }
    }

    private fun loading() = with(binding) {
        progressBar.root.viewState(true)
        notFoundText.viewState(state = false)
    }

    private fun error(message: String?) = with(binding) {
        root.showMessage(message)
        progressBar.root.viewState(false)
        notFoundText.viewState(state = false)
    }

    private fun success(data: List<Politician>) = with(binding) {
        politicianAdapter.submitList(data)
        binding.aspirantRecyclerView.adapter = politicianAdapter
        progressBar.root.viewState(false)
        notFoundText.viewState(state = false)
        aspirantRecyclerView.viewState(state = true)
    }

    private fun handleEmptyState() = with(binding) {
        progressBar.root.viewState(false)
        notFoundText.viewState(state = true)
        aspirantRecyclerView.viewState(state = false)
    }

    override fun onResume() {
        super.onResume()

        if (binding.include.searchEditText.text != null) {
            if (binding.include.searchEditText.text!!.isNotBlank()) {
                viewModel.searchPolitician(binding.include.searchEditText.text.toString())
            }
        }
    }
}
