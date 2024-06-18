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
import com.sevenexp.craftit.ui.update_picture.UpdatePictureActivity
import com.sevenexp.craftit.utils.FIELDTYPE
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import com.sevenexp.craftit.utils.ValidationTextWatcher
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel> { Locator.registerViewModelFactory }
    private val snackbar by lazy { TopSnackBar(binding.root) }
    var isLogin = false

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
                handleLoginState(state.resultLogin)
            }
        }
    }

    private fun handleLoginState(result: ResultState<String>) {
        when (result) {
            is ResultState.Success -> {
                setLoading(false)
                toUpdateProfilePicture()
            }

            is ResultState.Error -> toLogin()

            else -> Unit
        }
    }

    private fun handleRegisterState(result: ResultState<String>) {
        when (result) {
            is ResultState.Loading -> setLoading(true)
            is ResultState.Success -> doLogin()

            is ResultState.Error -> {
                setLoading(false)
                snackbar.error(result.message)
            }

            else -> setLoading(false)
        }
    }

    private fun doLogin() {
        if (!isLogin) {
            isLogin = true
            viewModel.login(binding.edEmail.text.toString(), binding.edPassword.text.toString())
        }
    }

    private fun setLoading(isEnabled: Boolean) {
        val visibility = if (isEnabled) View.VISIBLE else View.GONE

        binding.loading.loadingComponents.visibility = visibility
    }


    private fun setupButton() {
        with(binding) {
            btnBack.setOnClickListener { finish() }
            btnLogin.setOnClickListener { toUpdateProfilePicture() }
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

    private fun toUpdateProfilePicture() {
        val intent = Intent(this, UpdatePictureActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
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
