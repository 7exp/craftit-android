package com.sevenexp.craftit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import com.sevenexp.craftit.ui.image_search.ImageSearchActivity
import com.sevenexp.craftit.ui.search_result.SearchResultActivity
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.widget.CustomRecyclerView.ViewStatus
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { Locator.homeViewModelFactory })
    private val craftItemAdapter by lazy { CraftItemAdapter() }
    private val historyItemAdapter by lazy { HistoryItemAdapter() }
    private lateinit var binding: FragmentHomeBinding

    companion object {
        private const val UNAUTHORIZED_HTTP_RESPONSE = "http 401"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()
        setupButtons()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(requireContext(), SearchResultActivity::class.java)
                intent.putExtra("QUERY", query)
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
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
                recyclerView.adapter = craftItemAdapter
                recyclerView.layoutManager = LinearLayoutManager(context)
                setOnRetryClickListener {
                    viewModel.getHandicrafts()
                }
            }
            with(rvHistory) {
                adapter = historyItemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.getHandicraftsState.collect { state ->
                binding.greeting.text = getString(R.string.text_welcome, state.username)
                when (val result = state.resultGetHandicrafts) {
                    is ResultState.Success -> handleHandicraftSuccess(result)
                    is ResultState.Error -> handleHandicraftError(result)
                    is ResultState.Loading -> binding.rvForYou.showView(ViewStatus.LOADING)
                    is ResultState.Idle -> Unit
                }
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

    private fun hideHistory() {
        binding.rvHistory.visibility = View.GONE
        binding.rlHistory.visibility = View.GONE
    }

    private fun handleHandicraftSuccess(result: ResultState.Success<List<com.sevenexp.craftit.data.response.items.HandicraftItems>>) {
        result.data?.let { handicrafts ->
            craftItemAdapter.setData(handicrafts)
            binding.rvForYou.showView(ViewStatus.ON_DATA)
        } ?: binding.rvForYou.showView(ViewStatus.EMPTY)
    }

    private fun handleHandicraftError(result: ResultState.Error<*>) {
        if (result.message.isNotEmpty() && result.message.lowercase()
                .trim() == UNAUTHORIZED_HTTP_RESPONSE
        ) {
            doReLogin()
        } else {
            binding.rvForYou.showView(ViewStatus.ERROR)
        }
    }

    private fun doReLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            putExtra(LoginActivity.EXTRA_MESSAGE, getString(R.string.someone_else_login))
        }
        startActivity(intent)
        requireActivity().finish()
    }


}