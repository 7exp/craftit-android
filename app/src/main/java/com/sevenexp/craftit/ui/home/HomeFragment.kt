package com.sevenexp.craftit.ui.home

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
import com.sevenexp.craftit.databinding.FragmentHomeBinding
import com.sevenexp.craftit.ui.adapter.CraftItemAdapter
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.widget.CustomRecyclerView.ViewStatus
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { Locator.homeViewModelFactory })
    private val craftItemAdapter by lazy { CraftItemAdapter() }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.rvForYou.recyclerView) {
            adapter = craftItemAdapter
            layoutManager = LinearLayoutManager(context)
        }
        setupListener()
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.getHandicraftsState.collect { state ->
                binding.greeting.text = getString(R.string.text_welcome, state.username)
                when (state.resultGetHandicrafts) {
                    is ResultState.Success -> {
                        state.resultGetHandicrafts.data?.let { handicrafts ->
                            craftItemAdapter.setData(handicrafts)
                            binding.rvForYou.showView(ViewStatus.ON_DATA)
                        } ?: binding.rvForYou.showView(ViewStatus.EMPTY)
                    }

                    is ResultState.Error -> binding.rvForYou.showView(ViewStatus.ERROR)
                    is ResultState.Loading -> binding.rvForYou.showView(ViewStatus.LOADING)
                    is ResultState.Idle -> Unit
                }
            }
        }
    }
}