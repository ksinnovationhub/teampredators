package com.sdn.teampredators.polima.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentSignUpBinding
import com.sdn.teampredators.polima.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel by viewModels<SignUpViewModel>()
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        validateFields()
        setupDropDownMenus()
        setUpClickListeners()
    }

    private fun setUpClickListeners() = with(binding) {
        signUpButton.setOnClickListener {
            val email = etSignUpEmail.text.toString()
            val password = etSignUpPassword.text.toString().trim()
            lifecycleScope.launchWhenCreated {
                viewModel.doSignUp(email, password)
            }
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.signUp.collectLatest {
                when (it) {
                    is AuthenticationState.Success -> success()
                    is AuthenticationState.Error -> error(it.error)
                    is AuthenticationState.Loading -> loading()
                }
            }
        }
    }

    private fun setupDropDownMenus() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.drop_down_menu_item,
            resources.getStringArray(R.array.gender_array)
        )
        (binding.etGender as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun validateFields() = with(binding) {
        etFullName.addTextChangedListener(GenericTextWatcherSignUp(etFullName, null, this))
        etSignUpEmail.addTextChangedListener(GenericTextWatcherSignUp(etSignUpEmail, null, this))
        etSignUpPassword.addTextChangedListener(
            GenericTextWatcherSignUp(
                etSignUpPassword,
                null,
                this
            )
        )
        etNin.addTextChangedListener(GenericTextWatcherSignUp(etNin, null, this))
        etGender.addTextChangedListener(GenericTextWatcherSignUp(null, etGender, this))
    }

    private fun loading() = with(binding) {
        Timber.d("Loading...")
        progressBar.root.viewState(true)
    }

    private fun error(message: String?) = with(binding) {
        Timber.d("signInWithEmail:failure %s", message)
        root.showMessage(message ?: " ")
        progressBar.root.viewState(false)
    }

    private fun success() = with(binding) {
        Timber.d("signInWithEmail:success")
        progressBar.root.viewState(false)
    }


}