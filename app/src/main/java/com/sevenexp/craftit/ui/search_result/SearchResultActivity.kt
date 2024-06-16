package com.sevenexp.craftit.ui.search_result

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.databinding.ActivitySearchResultBinding
import com.sevenexp.craftit.ui.adapter.SearchAdapter
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.compressImage
import com.sevenexp.craftit.utils.toFile
import com.sevenexp.craftit.widget.CustomRecyclerView.ViewStatus
import kotlinx.coroutines.launch
import java.io.File

class SearchResultActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchResultBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchResultViewModel>(factoryProducer = { Locator.searchResulViewModelFactory })
    private val adapter by lazy { SearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val extraImage = intent.getStringExtra(EXTRA_IMAGE)
        val extraQuery = intent.getStringExtra(EXTRA_QUERY)
        if (!extraImage.isNullOrEmpty()) {
            val uri = extraImage.toUri()
            val myFile = uri.toFile(this)
            myFile.compressImage()
            doSearch(image = myFile)
        } else if (!extraQuery.isNullOrEmpty()) {
            binding.searchEditText.setText(extraQuery)
            doSearch(query = extraQuery)
        }

        setupRecyclerView()
        setupObserver()
    }

    private fun setupRecyclerView() {
        binding.rvSearchResult.recyclerView.adapter = adapter
        binding.rvSearchResult.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewModel.searchState.collect { result ->
                when (result.searchResult) {
                    is ResultState.Success -> {
                        val searchResult = result.searchResult.data
                        if (!searchResult.isNullOrEmpty()) {
                            binding.rvSearchResult.showView(ViewStatus.ON_DATA)
                            adapter.setData(searchResult)
                        } else {
                            binding.rvSearchResult.showView(ViewStatus.EMPTY)
                        }
                    }

                    else -> Unit
                }

            }
        }
    }

    private fun doSearch(query: String? = null, image: File? = null) {
        viewModel.search(query, image)
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_QUERY = "extra_query"
    }
}