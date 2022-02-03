package com.sdn.teampredators.polima.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentNotificationBinding
import com.sdn.teampredators.polima.utils.showNotificationDialog
import com.sdn.teampredators.polima.utils.viewBinding

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private val viewModel by viewModels<NotificationViewModel>()
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter {
            showNotificationDialog(notification = it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupObservers()
    }

    private fun setupAdapter() {
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = notificationAdapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is NotificationState.Content -> handleContent(it.notifications)
            }
        }
    }

    private fun handleContent(notifications: List<NotificationModel>) {
        notificationAdapter.submitList(notifications)
    }
}
