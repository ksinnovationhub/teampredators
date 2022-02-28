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
import com.google.firebase.auth.FirebaseAuth
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentPromiseDetailsBinding
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.ui.home.model.PromiseStatus
import com.sdn.teampredators.polima.utils.hideSoftKeyboard
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class PromiseDetailsFragment : Fragment(R.layout.fragment_promise_details) {

    private val binding by viewBinding(FragmentPromiseDetailsBinding::bind)
    private val args by navArgs<PromiseDetailsFragmentArgs>()

    private val viewModel by viewModels<PromiseDetailsViewModel>()
    private val commentsAdapter by lazy { CommentsAdapter() }

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()

        viewModel.setData(
            politicianId = args.politicianId,
            promiseId = args.promiseId
        )
        viewModel.getCurrentUserId()
    }

    private fun setupUi(promises: List<Promise>) = with(binding) {
        politicianName.text = args.politicianName
        politicianImage loadRoundImage args.politicianImage
        commentRecyclerView.adapter = commentsAdapter
        val tempPromise = promises.find {
            it.id == args.promiseId
        }
        val position = promises.indexOf(tempPromise)
        // Setup Toolbar
        toolbar.apply {
            title = promises[position].promise
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        val promise = promises[position]
        promiseTitle.text = promise.promise

        if (promise.status.equals(PromiseStatus.COMPLETED.value, ignoreCase = true)) {
            ratingGroup.isVisible = true
            promiseRatingGroup.isVisible = true
            if (promise.userIds.contains(firebaseAuth.uid)) {
                ratingGroup.isGone = true
                Timber.d("${promise.userIds}")
                Timber.d("${firebaseAuth.uid}")
            }
        }

        promiseRatingText.text = promise.averageRating.toString().take(3)
        promiseRating.rating = promise.averageRating
        totalRatings.text = promise.totalNumberOfRating.toString()
        promiseDescription.text = promise.promiseDescription
        promiseImage load promise.promiseImages.firstOrNull()
    }

    private fun setupClickListeners() = with(binding) {
        rating.setOnRatingBarChangeListener { _, rating, _ ->
            rateAction.isVisible = rating > 0
        }
        rateAction.setOnClickListener {
            viewModel.rateProject(rating.rating.toInt())
        }
        commentBox.addTextChangedListener {
            sendAction.isEnabled = it.isNullOrBlank().not()
        }
        sendAction.setOnClickListener {
            viewModel.addComment(comment = commentBox.text.toString().trim())
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is PromiseDetailsState.LoadUiContentsSuccess -> with(binding) {
                    setupUi(it.promises)
                    commentsAdapter.submitList(it.comments)
                    progressBar.isGone = true
                    progressLayout.root.isGone = true
                    commentBox.isEnabled = true
                    sendAction.isEnabled = true
                    commentBox.text.clear()
                }
                is PromiseDetailsState.Error -> {
                    binding.progressLayout.root.isGone = true
                    binding.rating.isEnabled = true
                    binding.rateAction.isEnabled = true
                }
                PromiseDetailsState.AddCommentsLoading -> {
                    binding.commentBox.isEnabled = false
                    binding.sendAction.isEnabled = false
                    hideSoftKeyboard(binding.root)
                }
                PromiseDetailsState.RatePromiseLoading -> {
                    binding.rating.isEnabled = false
                    binding.rateAction.isEnabled = false
                    hideSoftKeyboard(binding.root)
                }
                PromiseDetailsState.LoadUiContentsLoading -> {
                    binding.progressLayout.root.isVisible = true
                }
            }
        }
    }
}
