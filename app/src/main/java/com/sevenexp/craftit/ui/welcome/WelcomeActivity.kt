package com.sevenexp.craftit.ui.welcome

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityWelcomeBinding
import com.smarteist.autoimageslider.SliderView

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val items = arrayListOf(
            ContextCompat.getDrawable(this, R.drawable.welcome_image_1)!!,
            ContextCompat.getDrawable(this, R.drawable.welcome_image_2)!!,
            ContextCompat.getDrawable(this, R.drawable.welcome_image_3)!!,
        )
        val carouselAdapter = CarouselAdapter(this, items)
        binding.imageSlider.setSliderAdapter(carouselAdapter)
        binding.imageSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        binding.imageSlider.scrollTimeInSec = 5
        binding.imageSlider.isAutoCycle = true
        binding.imageSlider.startAutoCycle()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.imageSlider.isAutoCycle = false
        binding.imageSlider.stopAutoCycle()

    }
}