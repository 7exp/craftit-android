package com.sevenexp.craftit.ui.update_picture

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.databinding.ActivityUpdatePictureBinding

class UpdatePictureActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdatePictureBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
    }
}