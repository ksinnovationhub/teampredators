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
            val email = etSignUpEmail.text.toString().trim()
            val password = etSignUpPassword.text.toString()
            val nin = etNin.text.toString().trim()
            val fullName = etFullName.text.toString().trim()
            val gender = etGender.text.toString().trim()
            viewModel.doSignUp(
                email = email,
                password = password,
                fullName = fullName,
                nin = nin,
                gender = gender
            )
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
        progressBar.root.viewState(true)
        hideSoftKeyboard(binding.root)
    }

    private fun error(message: String?) = with(binding) {
        root.showMessage(message)
        progressBar.root.viewState(false)
    }

    private fun success() = with(binding) {
        progressBar.root.viewState(false)
        showGenericDialog(
            message = getString(R.string.verificaation_message),
            negativeButtonText = null,
            positiveButtonText = getString(R.string.ok),
            positiveButtonCallback = {}
        )
    }
}
