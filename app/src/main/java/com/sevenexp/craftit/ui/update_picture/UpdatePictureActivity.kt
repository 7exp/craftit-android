package com.sevenexp.craftit.ui.update_picture

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.databinding.ActivityUpdatePictureBinding
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import java.io.File

class UpdatePictureActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdatePictureBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UpdateProfilePictureViewModel>(factoryProducer = { Locator.updateProfilePictureFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
    }

    private fun setupButton() {
        with(binding) {
            btnSelectImage.setOnClickListener { getPicture() }
            btnUpload.setOnClickListener { doUpload() }
            btnSkip.setOnClickListener { toLogin() }
        }
    }

    private fun getPicture(){

    }

    private fun doUpload() {
//        viewModel.updateImage(file)
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}