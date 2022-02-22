package com.sdn.teampredators.polima.ui.aspirant.all_promises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentAllPromisesBinding
import com.sdn.teampredators.polima.ui.aspirant.AspirantViewModelFactory
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.ui.home.model.PromiseStatus
import com.sdn.teampredators.polima.utils.viewBinding

class AllPromisesFragment : Fragment(R.layout.fragment_all_promises) {

    private val binding by viewBinding(FragmentAllPromisesBinding::bind)
    private val args by navArgs<AllPromisesFragmentArgs>()
    private val viewModel by viewModels<AllPromisesViewModel> {
        AspirantViewModelFactory(args.politicianItem)
    }
    private val projectAdapter: AllPromisesAdapter = AllPromisesAdapter {  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupAdapter()
        setupObservers()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupAdapter() = with(binding) {
        projectRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        projectRecyclerView.adapter = projectAdapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AllPromisesState.Content -> handleContent(it.promises)
            }
        }
    }

    private fun setupClickListeners() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_completed -> viewModel.filter(PromiseStatus.COMPLETED)
                R.id.chip_ongoing -> viewModel.filter(PromiseStatus.ONGOING)
                R.id.chip_not_started -> viewModel.filter(PromiseStatus.NOT_STARTED)
                R.id.chip_abandoned -> viewModel.filter(PromiseStatus.ABANDONED)
                else -> viewModel.filter(PromiseStatus.ALL)
            }
        }
    }

    private fun handleContent(promises: List<Promise>) {
        projectAdapter.submitList(promises)
    }
}
