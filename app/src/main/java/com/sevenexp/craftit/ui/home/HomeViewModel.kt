package com.sevenexp.craftit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.domain.usecase.GetAllHistoryUseCase
import com.sevenexp.craftit.domain.usecase.GetFypUseCase
import com.sevenexp.craftit.domain.usecase.GetUserUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFypUseCase: GetFypUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase
) : ViewModel() {

    data class HomeViewState(
        val resultGetFyp: PagingData<FypItems> = PagingData.empty(),
        val resultGetHistory: ResultState<List<HistoryEntity>> = ResultState.Idle(),
        val username: String = ""
    )

    private val _getFypState = MutableStateFlow(HomeViewState())
    val getFypState = _getFypState

    init {
        getUsername()
        getAllHistory()
    }

    private fun getAllHistory() {
        viewModelScope.launch {
            getAllHistoryUseCase().collect { result ->
                _getFypState.update { it.copy(resultGetHistory = result) }
            }
        }
    }

    fun getFyp() {
        viewModelScope.launch {
            getFypUseCase().cachedIn(viewModelScope).collect { fyps ->
                _getFypState.update { it.copy(resultGetFyp = fyps) }
            }
        }
    }

    private fun getUsername() {
        viewModelScope.launch {
            getUserUseCase().collect { user ->
                _getFypState.update {
                    it.copy(username = user.name)
                }
            }
        }
    }

    class Factory(
        private val getFypUseCase: GetFypUseCase,
        private val getUserUseCase: GetUserUseCase,
        private val getAllHistoryUseCase: GetAllHistoryUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(getFypUseCase, getUserUseCase, getAllHistoryUseCase) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }

}

