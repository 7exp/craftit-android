package com.sevenexp.craftit.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityLoginBinding
import com.sevenexp.craftit.ui.MainActivity
import com.sevenexp.craftit.ui.auth.register.RegisterActivity
import com.sevenexp.craftit.utils.FIELDTYPE
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.ValidationTextWatcher
import com.sevenexp.craftit.utils.TopSnackBar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel> { Locator.loginViewModelFactory }
    private val snackbar by lazy { TopSnackBar(binding.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_MESSAGE)?.let { snackbar.info(it) }

        setupButton()
        setupListener()
    }

    private fun setupButton() {
        with(binding) {
            btnLogin.setOnClickListener { checkLogin() }
            btnRegister.setOnClickListener { toRegister() }
            btnBack.setOnClickListener { finish() }
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

    private fun checkLogin() {
        with(binding) {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            if (isValidInput()) {
                viewModel.login(email, password)
            }
        }
    }

    private fun isValidInput(): Boolean {
        val errorMessage = getString(R.string.fill_all_fields)

        return with(binding) {
            validateField(edEmail, errorMessage) &&
                    validateField(edPassword, errorMessage) &&
                    edEmail.error.isNullOrEmpty() &&
                    edPassword.error.isNullOrEmpty()
        }
    }

    private fun validateField(field: EditText, errorString: String): Boolean {
        return if (field.text.toString().isEmpty()) {
            field.error = errorString
            field.requestFocus()
            false
        } else {
            true
        }
    }


    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                handleLoginState(state.resultLogin)
            }
        }
    }

    private fun handleLoginState(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> setLoading(true)
            is ResultState.Success -> {
                setLoading(false)
                toMain()
            }

            is ResultState.Error -> {
                setLoading(false)
                showError(result.message)
            }

            else -> Unit
        }
    }

    private fun showError(message: String) {
        if (message.trim().lowercase() == "http 403") {
            snackbar.error(getString(R.string.invalid_password))
            binding.edPassword.requestFocus()
        } else if (message.trim().lowercase() == "http 404") {
            snackbar.error(getString(R.string.email_not_registered))
            binding.edEmail.requestFocus()
        } else if (message.trim().lowercase() == "timeout") {
            snackbar.error(getString(R.string.timeout_error))
        } else {
            snackbar.error(message)
        }
    }

    private fun setLoading(isEnabled: Boolean) {
        val visibility = if (isEnabled) View.VISIBLE else View.GONE
        binding.loading.loadingComponents.visibility = visibility
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toRegister(){
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }
}