package com.sdn.teampredators.polima.ui.aspirant.promise_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentPromiseDetailsBinding
import com.sdn.teampredators.polima.utils.load
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding

class PromiseDetailsFragment : Fragment(R.layout.fragment_promise_details) {

    private val binding by viewBinding(FragmentPromiseDetailsBinding::bind)
    private val args by navArgs<PromiseDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupUi()
    }

    private fun setupUi() = with(binding) {
        politicianName.text = args.politicianName
        politicianImage loadRoundImage args.politicianImage

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
}
