package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityDetailBinding
import kotlinx.parcelize.Parcelize

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>(factoryProducer = { Locator.DetailViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val craftId = intent.getStringExtra(EXTRA_CRAFT_ID)
        val title = intent.getStringExtra(EXTRA_TITLE)

        if (craftId.isNullOrEmpty() || title.isNullOrEmpty()) {
            Log.e("DetailActivity", "Invalid craft id, or title")
            Log.e("DetailActivity", "craftId: $craftId, title: $title")
            finish()
            return
        }

        viewModel.getDetail(craftId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailFragment.newInstance())
            .commit()
    }

    companion object {
        const val EXTRA_CRAFT_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
    }
}