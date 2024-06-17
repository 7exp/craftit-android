package com.sevenexp.craftit.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityRegisterBinding
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import com.sevenexp.craftit.utils.FIELDTYPE
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import com.sevenexp.craftit.utils.ValidationTextWatcher
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel> { Locator.registerViewModelFactory }
    private val snackbar by lazy { TopSnackBar(binding.root, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupFieldCheckers()
        setupButton()
        setupListener()
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                handleRegisterState(state.resultRegister)
            }
        }
    }

    private fun handleRegisterState(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> setLoading(true)
            is ResultState.Success -> {
                toLogin(true)
                setLoading(false)
            }

            is ResultState.Error -> {
                setLoading(false)
                snackbar.error(result.message)
            }

            else -> setLoading(false)
        }
    }

    private fun setLoading(isEnabled: Boolean) {
        val visibility = if (isEnabled) View.VISIBLE else View.GONE
        binding.loading.loadingComponents.visibility = visibility
    }


    private fun setupButton() {
        with(binding) {
            btnBack.setOnClickListener { finish() }
            btnLogin.setOnClickListener { toLogin() }
            btnRegister.setOnClickListener {
                if (btnRegister.isEnabled.not()) return@setOnClickListener
                if (isInputValid()) {
                    viewModel.register(
                        edName.text.toString(),
                        edEmail.text.toString(),
                        edPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun toLogin(isSuccess: Boolean = false) {
        val intent = Intent(this, LoginActivity::class.java)

        if (isSuccess) intent.putExtra(
            LoginActivity.EXTRA_MESSAGE,
            getString(R.string.register_success)
        )

        startActivity(intent)
        finish()
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            return when {
                edName.text.isNullOrEmpty() -> {
                    edName.error = getString(R.string.not_empty)
                    false
                }

                edPassword.text.isNullOrEmpty() -> {
                    edPassword.error = getString(R.string.not_empty)
                    false
                }

                edEmail.text.isNullOrEmpty() -> {
                    edEmail.error = getString(R.string.not_empty)
                    false
                }

                else -> true
            }.also { isValid ->
                if (isValid.not()) {
                    snackbar.error(getString(R.string.fill_all_fields))
                }
            }
        }
    }

    private fun setupFieldCheckers() {
        with(binding) {
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

}
