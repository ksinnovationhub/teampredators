package com.sdn.teampredators.polima.ui.aspirant.promise_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentPromiseDetailsBinding
import com.sdn.teampredators.polima.utils.hideSoftKeyboard
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseDetailsFragment : Fragment(R.layout.fragment_promise_details) {

    private val binding by viewBinding(FragmentPromiseDetailsBinding::bind)
    private val args by navArgs<PromiseDetailsFragmentArgs>()

    private val viewModel by viewModels<PromiseDetailsViewModel>()
    private val commentsAdapter by lazy { CommentsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupUi()
        setupClickListeners()
        setupObservers()
        viewModel.setPromiseId(args.promise.id)
        viewModel.getCurrentUserId()
    }

    private fun setupUi() = with(binding) {
        politicianName.text = args.politicianName
        politicianImage loadRoundImage args.politicianImage
        commentRecyclerView.adapter = commentsAdapter

        val promise = args.promise
        promiseTitle.text = promise.promise
        promiseDescription.text = promise.promiseDescription
        promiseImage load promise.promiseImages.firstOrNull()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = args.promise.promise
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListeners() = with(binding) {
        rating.setOnRatingBarChangeListener { _, rating, _ ->
            rateAction.isVisible = rating > 0
        }
        rateAction.setOnClickListener {
            // TODO: Rate project
            rating.rating
        }
        commentBox.addTextChangedListener {
            sendAction.isEnabled = it.isNullOrBlank().not()
        }
        sendAction.setOnClickListener {
            viewModel.addComment(comment = commentBox.text.toString())
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is PromiseDetailsState.GetCommentsSuccess -> with(binding) {
                    commentsAdapter.submitList(it.comments)
                    progressBar.isGone = true
                    progressLayout.root.isGone = true
                    commentBox.isEnabled = true
                    sendAction.isEnabled = true
                    commentBox.text.clear()
                }
                is PromiseDetailsState.Error -> {
                    binding.progressLayout.root.isGone = true
                }
                PromiseDetailsState.GetUserSuccess -> {
                    binding.progressLayout.root.isGone = true
                }
                PromiseDetailsState.GetUserLoading -> {
                    binding.progressLayout.root.isVisible = true
                    hideSoftKeyboard(binding.root)
                }
                PromiseDetailsState.GetCommentsLoading -> {
                    binding.progressBar.isVisible = true
                    hideSoftKeyboard(binding.root)
                }
                PromiseDetailsState.AddCommentsSuccess -> {
                    binding.commentBox.isEnabled = true
                    binding.sendAction.isEnabled = true
                }
                PromiseDetailsState.AddCommentsLoading -> {
                    binding.commentBox.isEnabled = false
                    binding.sendAction.isEnabled = false
                    hideSoftKeyboard(binding.root)
                }
            }
        }
    }
}
