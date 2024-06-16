package com.sevenexp.craftit.ui.search_result

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sevenexp.craftit.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchResultBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val extraImage = intent.getStringExtra(EXTRA_IMAGE)
        val extraQuery = intent.getStringExtra(EXTRA_QUERY)
        if (!extraImage.isNullOrEmpty()) {
            doSearch(extraImage, SearchType.IMAGE)
        } else if (!extraQuery.isNullOrEmpty()) {
            doSearch(extraQuery, SearchType.TEXT)
        }

    }

    private fun doSearch(query: String, type: SearchType) {

    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_QUERY = "extra_query"

        private enum class SearchType {
            IMAGE, TEXT
        }
    }
}