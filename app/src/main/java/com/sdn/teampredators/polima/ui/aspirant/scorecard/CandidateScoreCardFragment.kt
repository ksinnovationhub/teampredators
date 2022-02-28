package com.sdn.teampredators.polima.ui.aspirant.scorecard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentCandidateScoreCardBinding
import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.utils.loadRoundImage
import com.sdn.teampredators.polima.utils.viewBinding

class CandidateScoreCardFragment : Fragment(R.layout.fragment_candidate_score_card) {

    private val binding by viewBinding(FragmentCandidateScoreCardBinding::bind)
    private val args by navArgs<CandidateScoreCardFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi(args.politician)
    }

    private fun setupUi(politician: Politician) = with(binding) {
        candidateImage loadRoundImage politician.photoUrl
        candidateName.text = politician.fullName
        candidatePosition.text = politician.position
        candidateParty.text = politician.party
    }
}
