package com.sevenexp.craftit.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sevenexp.craftit.data.source.database.entity.RecentEntity
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity
import com.sevenexp.craftit.domain.usecase.GetRecentUseCase
import com.sevenexp.craftit.domain.usecase.GetTrendingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val getTrendingUseCase: GetTrendingUseCase,
    private val getRecentUseCase: GetRecentUseCase
) : ViewModel() {

    data class TrendingViewState(
        val resultGetTrending: PagingData<TrendingEntity> = PagingData.empty(),
        val resultGetRecent: PagingData<RecentEntity> = PagingData.empty()
    )

    private val _getTrendingState = MutableStateFlow(TrendingViewState())
    val getTrendingState = _getTrendingState

    init {
        getTrending()
        getRecent()
    }

    private fun getRecent() {
        viewModelScope.launch {
            getRecentUseCase().cachedIn(viewModelScope).collect { recents ->
                _getTrendingState.update { it.copy(resultGetRecent = recents) }
            }
        }
    }

    private fun getTrending() {
        viewModelScope.launch {
            getTrendingUseCase().cachedIn(viewModelScope).collect { trends ->
                _getTrendingState.update { it.copy(resultGetTrending = trends) }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTrendingUseCase: GetTrendingUseCase,
        private val getRecentUseCase: GetRecentUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscoveryViewModel::class.java)) {
                return DiscoveryViewModel(getTrendingUseCase, getRecentUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}