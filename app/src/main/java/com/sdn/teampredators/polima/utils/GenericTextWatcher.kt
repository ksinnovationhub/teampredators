package com.sdn.teampredators.polima.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.FragmentSignInBinding
import com.sdn.teampredators.polima.databinding.FragmentSignUpBinding

/**
 * Please Don't Mind this class
 **/

class GenericTextWatcherSignIn internal constructor(
    private val currentView: TextInputEditText,
    private val binding: FragmentSignInBinding
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) = with(binding) {
        val text = p0.toString()
        val emailPattern =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        when (currentView.id) {

            R.id.et_email -> {
                when {
                    text.isEmpty() -> {
                        tiEmail.error = "Please enter an email address"
                        signInButton.isEnabled = false
                    }
                    !text.matches(emailPattern.toRegex()) -> {
                        tiEmail.error = "wrong email address"
                        signInButton.isEnabled = false
                    }
                    else -> {
                        tiEmail.error = null
                        tiPassword.isEnabled = true
                    }
                }
            }
            R.id.et_password -> {
                when {
                    text.isEmpty() -> {
                        tiPassword.error = "Please Enter a password"
                        signInButton.isEnabled = false
                    }
                    text.length < 6 -> {
                        tiPassword.error = "Password must be greater than six"
                        signInButton.isEnabled = false
                    }
                    else -> {
                        tiPassword.error = null
                        signInButton.isEnabled = true
                    }
                }
            }
        }
    }
}

class GenericTextWatcherSignUp internal constructor(
    private val currentView: TextInputEditText?,
    private val currentView2: AutoCompleteTextView?,
    private val binding: FragmentSignUpBinding
): TextWatcher{
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) = with(binding) {
        val text = p0.toString()
        val emailPattern =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        when(currentView?.id){
            R.id.et_full_name -> {
                when{
                    text.isEmpty() -> {
                        tiFullName.error = "Please Enter a Full Name"
                        signUpButton.isEnabled = false
                    }
                    text.length < 6 ->{
                        tiFullName.error = "Full Name Must be greater than 6 characters"
                        signUpButton.isEnabled = false
                    }
                    else -> {
                        tiFullName.error = null
                        tiSignUpEmail.isEnabled = true
                    }
                }
            }
            R.id.et_sign_up_email -> {
                when {
                    text.isEmpty() -> {
                        tiSignUpEmail.error = "Please enter an email address"
                        signUpButton.isEnabled = false
                    }
                    !text.matches(emailPattern.toRegex()) -> {
                        tiSignUpEmail.error = "wrong email address"
                        signUpButton.isEnabled = false
                    }
                    else -> {
                        tiSignUpEmail.error = null
                        tiSignUpPassword.isEnabled = true
                    }
                }
            }
            R.id.et_sign_up_password -> {
                when {
                    text.isEmpty() -> {
                        tiSignUpPassword.error = "Please Enter a password"
                        signUpButton.isEnabled = false
                    }
                    text.length < 6 -> {
                        tiSignUpPassword.error = "Password must be greater than six"
                        signUpButton.isEnabled = false
                    }
                    else -> {
                        tiSignUpPassword.error = null
                        tiNin.isEnabled = true
                    }
                }
            }
            R.id.et_nin -> {
                when {
                    text.isEmpty() -> {
                        tiNin.error = "Please Enter your NIN Number"
                        signUpButton.isEnabled = false
                    }
                    text.length != 11 -> {
                        tiNin.error = "NIN Number should be 11 digits"
                        signUpButton.isEnabled = false
                    }
                    else -> {
                        tiNin.error = null
                        tiGender.isEnabled = true
                    }
                }
            }
        }
        when(currentView2?.id){
            R.id.et_gender ->{
                when {
                    text.isEmpty() -> {
                        signUpButton.isEnabled = false
                    }
                    else -> {
                        tiGender.isEnabled = true
                        signUpButton.isEnabled = true
                    }
                }
            }
        }
    }

}