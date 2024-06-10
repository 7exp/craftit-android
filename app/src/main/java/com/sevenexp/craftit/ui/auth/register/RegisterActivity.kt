package com.sevenexp.craftit.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel> { Locator.registerViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupFieldCheckers()
        setupRegisterButton()
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            if (isInputValid()) {
                viewModel.register(
                    binding.edName.text.toString(),
                    binding.edEmail.text.toString(),
                    binding.edPassword.text.toString()
                )
            } else {
                // TODO: Handle error
            }
        }
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            return !edName.text.isNullOrEmpty() &&
                    !edPassword.text.isNullOrEmpty() &&
                    !edEmail.text.isNullOrEmpty() &&
                    edPassword.error.isNullOrEmpty() &&
                    edEmail.error.isNullOrEmpty()
        }
    }

    private fun setupFieldCheckers() {
        binding.apply {
            edEmail.addTextChangedListener(
                ValidationTextWatcher(
                    FIELDTYPE.EMAIL,
                    getString(R.string.not_an_email),
                    edEmail
                )
            )
            edPassword.addTextChangedListener(
                ValidationTextWatcher(
                    FIELDTYPE.PASSWORD,
                    getString(R.string.not_a_password),
                    edPassword
                )
            )
        }
    }

    private inner class ValidationTextWatcher(
        private val fieldType: FIELDTYPE,
        private val errorMessage: String,
        private val editText: EditText
    ) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty() && !isInputValid(s.toString(), fieldType)) {
                editText.error = errorMessage
            } else {
                editText.error = null
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // No implementation needed
        }

        private fun isInputValid(input: String, fieldType: FIELDTYPE): Boolean {
            val regex = when (fieldType) {
                FIELDTYPE.EMAIL -> android.util.Patterns.EMAIL_ADDRESS.pattern()
                FIELDTYPE.PASSWORD -> "^.{8,}$"
            }
            return input.matches(regex.toRegex())
        }
    }

    companion object {
        enum class FIELDTYPE {
            EMAIL, PASSWORD
        }
    }
}
