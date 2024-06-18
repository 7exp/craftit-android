package com.sevenexp.craftit.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.databinding.FragmentHomeBinding
import com.sevenexp.craftit.ui.adapter.CraftItemAdapter
import com.sevenexp.craftit.ui.adapter.HistoryItemAdapter
import com.sevenexp.craftit.ui.adapter.LoadingAdapter
import com.sevenexp.craftit.ui.image_search.ImageSearchActivity
import com.sevenexp.craftit.ui.search.SearchActivity
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { Locator.homeViewModelFactory })
    private val historyItemAdapter by lazy { HistoryItemAdapter() }
    private val fypAdapter by lazy { CraftItemAdapter() }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater, container, false).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()
        setupButtons()
        setupSearchView()
    }


    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchbar)
            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                searchbar.setText(searchView.text)
                searchView.hide()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    moveToSearchResult(searchView.text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun moveToSearchResult(query: String) {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_QUERY, query)
        startActivity(intent)
    }

    private fun setupButtons() {
        with(binding) {
            btnImageSearch.setOnClickListener {
                startActivity(Intent(requireContext(), ImageSearchActivity::class.java))
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            with(rvForYou) {
                adapter =
                    fypAdapter.withLoadStateFooter(footer = LoadingAdapter { fypAdapter.retry() })
                layoutManager = LinearLayoutManager(context)
            }
            with(rvHistory) {
                adapter = historyItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.getFypState.collect { state ->
                binding.greeting.text = getString(R.string.text_welcome, state.username)
                fypAdapter.submitData(lifecycle, state.resultGetFyp)
                Log.d("HOME FRAGMENT", state.resultGetFyp.toString())

                when (val result = state.resultGetHistory) {
                    is ResultState.Success -> handleHistorySuccess(result)
                    is ResultState.Error -> Unit
                    is ResultState.Loading -> Unit
                    is ResultState.Idle -> Unit
                }
            }
        }
    }

    private fun handleHistorySuccess(result: ResultState.Success<List<HistoryEntity>>) {
        val visible = View.VISIBLE
        result.data?.let { histories ->
            if (histories.isEmpty()) {
                hideHistory()
            } else {
                binding.rvHistory.visibility = visible
                binding.rlHistory.visibility = visible
                historyItemAdapter.setData(histories)
            }
        } ?: {
            hideHistory()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFyp()
    }

    private fun hideHistory() {
        binding.rvHistory.visibility = View.GONE
        binding.rlHistory.visibility = View.GONE
    }

}