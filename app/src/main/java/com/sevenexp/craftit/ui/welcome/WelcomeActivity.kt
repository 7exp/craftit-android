package com.sevenexp.craftit.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityWelcomeBinding
import com.sevenexp.craftit.ui.auth.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener { toActivity(LoginActivity::class.java) }
            btnRegister.setOnClickListener { }
        }

        setupSlider()
        setupTitle()
    }

    private fun toActivity(name: Class<*>) {
        startActivity(Intent(this, name))
    }

    private fun setupSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.welcome_image_1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.welcome_image_2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.welcome_image_3, ScaleTypes.CENTER_CROP))
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupTitle() {
        val spannable = SpannableString(getString(R.string.text_welcome_title))
        val primaryColor = ContextCompat.getColor(this, R.color.primary)

        val lastTwoWord = "\\s\\S+\\s\\S+$".toRegex().find(spannable.toString())!!.range.first
        val firstDot = spannable.indexOf(".")
        val spaceBeforeDot = spannable.lastIndexOf(" ", firstDot)

        spannable.setSpan(
            ForegroundColorSpan(primaryColor),
            lastTwoWord, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(primaryColor),
            spaceBeforeDot, firstDot,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvTitle.text = spannable
    }
}