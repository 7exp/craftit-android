package com.sevenexp.craftit.ui.discovery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.databinding.FragmentDiscoveryBinding
import com.sevenexp.craftit.ui.adapter.RecentAdapter
import com.sevenexp.craftit.ui.adapter.TrendingAdapter
import com.sevenexp.craftit.ui.search.SearchActivity
import kotlinx.coroutines.launch

class DiscoveryFragment : Fragment() {
    private val viewModel by viewModels<DiscoveryViewModel>(factoryProducer = { Locator.DiscoveryViewModelFactory })
    private val trendingAdapter by lazy { TrendingAdapter() }
    private val recentAdapter by lazy { RecentAdapter() }
    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        setupListener()
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

    private fun setupButton() {
        with(binding) {
            btnImageSearch.setOnClickListener {
                val intent = Intent(requireContext(), SearchActivity::class.java)
                intent.putExtra(SearchActivity.EXTRA_TAKE, true)
                startActivity(intent)
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.getTrendingState.collect { state ->
                trendingAdapter.submitData(lifecycle, state.resultGetTrending)
                recentAdapter.submitData(lifecycle, state.resultGetRecent)
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvRecent.adapter = recentAdapter
            rvTrending.adapter = trendingAdapter
            rvTrending.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvRecent.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun moveToSearchResult(query: String) {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_QUERY, query)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}