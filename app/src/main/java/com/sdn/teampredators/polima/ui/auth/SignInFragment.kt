package com.sdn.teampredators.polima.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentSignInBinding
import com.sdn.teampredators.polima.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setUpClickListeners()
        validateFields()
    }

    private fun setUpClickListeners() = with(binding) {
        signUpButton.setOnClickListener {
            viewModel.toSignUp()
        }
        signInButton.setOnClickListener {
            val email0 = etEmail.text.toString()
            val password0 = etPassword.text.toString().trim()
            viewModel.doSignIn(email0, password0)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.signIn.collectLatest { state ->
                when (state) {
                    is AuthenticationState.Success -> success()
                    is AuthenticationState.Error -> error(state.error)
                    is AuthenticationState.Loading -> loading()
                }
            }
        }
        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                is GenericActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun validateFields() = with(binding) {
        etEmail.addTextChangedListener(GenericTextWatcherSignIn(etEmail, this))
        etPassword.addTextChangedListener(GenericTextWatcherSignIn(etPassword, this))
    }

    private fun loading() = with(binding) {
        progressBar.root.viewState(true)
    }

    private fun error(message: String?) = with(binding) {
        root.showMessage(message)
        progressBar.root.viewState(false)
    }

    private fun success() = with(binding) {
        progressBar.root.viewState(false)
    }
}
