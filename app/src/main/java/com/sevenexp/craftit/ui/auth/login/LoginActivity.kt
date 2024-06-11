package com.sevenexp.craftit.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityLoginBinding
import com.sevenexp.craftit.ui.MainActivity
import com.sevenexp.craftit.utils.FIELDTYPE
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.ValidationTextWatcher
import com.sevenexp.craftit.widget.TopSnackBar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel> { Locator.loginViewModelFactory }
    private val snackbar by lazy { TopSnackBar(binding.root, this) }

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
            btnLogin.setOnClickListener {
                if (btnLogin.isEnabled.not()) return@setOnClickListener

                if (!isValidInput()) return@setOnClickListener

                doLogin()
            }
            edEmail.addTextChangedListener(ValidationTextWatcher(FIELDTYPE.EMAIL, getString(R.string.not_an_email), edEmail))
            edPassword.addTextChangedListener(ValidationTextWatcher(FIELDTYPE.PASSWORD, getString(R.string.not_a_password), edPassword))
        }
    }

    private fun isValidInput(): Boolean {
        if (!isValidEmail() || !isValidPassword()) {
            snackbar.error(getString(R.string.fill_all_fields))
            return false
        }
        return true
    }

    private fun isValidEmail(): Boolean {
        return if (binding.edEmail.text.isNullOrEmpty()) {
            binding.edEmail.error = getString(R.string.error_email_empty)
            binding.edEmail.requestFocus()
            false
        } else true
    }

    private fun isValidPassword(): Boolean {
        return if (binding.edPassword.text.isNullOrEmpty()) {
            binding.edPassword.error = getString(R.string.error_password_empty)
            binding.edPassword.requestFocus()
            false
        } else true
    }

    private fun doLogin() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        viewModel.login(email, password)
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                handleLoginState(state.resultLogin)
            }
        }
    }

    private fun handleLoginState(result: ResultState<String>) {
        setLoginButtonEnabled(false)
        when (result) {
            is ResultState.Loading -> setLoginButtonEnabled(false)
            is ResultState.Success -> {
                toMain()
                setLoginButtonEnabled(true)
            }
            is ResultState.Error -> {
                snackbar.error(result.message)
                setLoginButtonEnabled(true)
            }
            else -> setLoginButtonEnabled(true)
        }
    }

    private fun setLoginButtonEnabled(isEnabled: Boolean) {
        binding.btnLogin.isEnabled = isEnabled
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }
}