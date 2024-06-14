package com.sevenexp.craftit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.response.items.HandicraftItem
import com.sevenexp.craftit.databinding.FragmentHomeBinding
import com.sevenexp.craftit.ui.adapter.CraftItemAdapter
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.widget.CustomRecyclerView.ViewStatus
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { Locator.homeViewModelFactory })
    private val craftItemAdapter by lazy { CraftItemAdapter() }
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
    }

    private fun setupRecyclerView() {
        with(binding.rvForYou) {
            recyclerView.adapter = craftItemAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            setOnRetryClickListener {
                viewModel.getHandicrafts()
            }
        }
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.getHandicraftsState.collect { state ->
                binding.greeting.text = getString(R.string.text_welcome, state.username)
                when (val result = state.resultGetHandicrafts) {
                    is ResultState.Success -> handleSuccess(result)
                    is ResultState.Error -> handleError(result)
                    is ResultState.Loading -> binding.rvForYou.showView(ViewStatus.LOADING)
                    is ResultState.Idle -> Unit
                }
            }
        }
    }

    private fun handleSuccess(result: ResultState.Success<List<HandicraftItem>>) {
        result.data?.let { handicrafts ->
            craftItemAdapter.setData(handicrafts)
            binding.rvForYou.showView(ViewStatus.ON_DATA)
        } ?: binding.rvForYou.showView(ViewStatus.EMPTY)
    }

    private fun handleError(result: ResultState.Error<*>) {
        if (result.message.isNotEmpty() && result.message.lowercase()
                .trim() == UNAUTHORIZED_HTTP_RESPONSE
        ) {
            doReLogin()
        }
        binding.rvForYou.showView(ViewStatus.ERROR)
    }

    private fun doReLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            putExtra(LoginActivity.EXTRA_MESSAGE, getString(R.string.someone_else_login))
        }
        startActivity(intent)
        requireActivity().finish()
    }
}