package com.sdn.teampredators.polima.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.data.models.AspirantDto
import com.sdn.teampredators.polima.databinding.FragmentHomeBinding
import com.sdn.teampredators.polima.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val firestoreAdapter: PolimaFirestoreAdapter by lazy {
        PolimaFirestoreAdapter()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAspirantData()
        setUpObservers()
    }

    private fun setUpAspirantData() {
        viewModel.getData()
    }

    private fun setUpObservers(){
        lifecycleScope.launchWhenStarted {
            viewModel.aspirantData.collectLatest {
                when(it) {
                    is ListResult.Success-> success(it.list)
                    is ListResult.Error -> error(it.error)
                    is ListResult.Loading -> loading()
                }
            }
        }
    }

    private fun loading() = with(binding) {
        progressBar.root.viewState(true)
    }

    private fun error(message: String?) = with(binding) {
        root.showMessage(message ?: " ")
        progressBar.root.viewState(false)
    }

    private fun success(data: List<AspirantDto>) = with(binding) {
        firestoreAdapter.submitList(data)
        binding.aspirantRecyclerView.adapter = firestoreAdapter
        progressBar.root.viewState(false)
    }

}
