package com.sdn.teampredators.polima.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentForgotPasswordBinding
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)
    private val viewModel by viewModels<ForgotPasswordViewModel>()
}
