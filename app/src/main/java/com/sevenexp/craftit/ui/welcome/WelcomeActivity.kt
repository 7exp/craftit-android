package com.sevenexp.craftit.ui.welcome

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateOvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityWelcomeBinding
import com.sevenexp.craftit.ui.MainActivity
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import com.sevenexp.craftit.utils.ResultState

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<WelcomeViewModel>(factoryProducer = { Locator.welcomeViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContentView(binding.root)

        setupSplashScreen()

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

    private fun setupSplashScreen() {
        val content: View = binding.root
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val isLoggedIn = viewModel.isLoggedIn.value

                if (isLoggedIn.resultGetUser is ResultState.Success) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    if (isLoggedIn.resultGetUser.data == true) {
                        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    }
                    return true
                } else {
                    return false
                }
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setupSplashScreenExitAnimation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupSplashScreenExitAnimation() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView, View.TRANSLATION_Y, 0f, splashScreenView.height.toFloat()
            )
            val alpha = ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f)

            slideUp.interpolator = AnticipateOvershootInterpolator()
            slideUp.duration = ANIMATION_DURATION
            alpha.duration = ANIMATION_DURATION

            slideUp.doOnEnd { splashScreenView.remove() }

            alpha.start()
            slideUp.start()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 500L
    }

}