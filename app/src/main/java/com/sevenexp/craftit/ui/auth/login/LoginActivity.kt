package com.sevenexp.craftit.ui.auth.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.databinding.ActivityLoginBinding
import com.sevenexp.craftit.widget.TopSnackBar

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val snackbar by lazy { TopSnackBar(binding.root, this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val extraMessage: String? = intent.getStringExtra(EXTRA_MESSAGE)
        if (extraMessage != null) {
            snackbar.info(extraMessage)
        }
    }

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
    }
}