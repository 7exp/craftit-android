package com.sevenexp.craftit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.domain.usecase.GetAllHandicraftUseCase
import com.sevenexp.craftit.domain.usecase.GetAllHistoryUseCase
import com.sevenexp.craftit.domain.usecase.GetUserUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val handicraftUseCase: GetAllHandicraftUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase
) : ViewModel() {

    data class HomeViewState(
        val resultGetHandicrafts: ResultState<List<com.sevenexp.craftit.data.response.items.HandicraftItems>> = ResultState.Idle(),
        val resultGetHistory: ResultState<List<HistoryEntity>> = ResultState.Idle(),
        val username: String = ""
    )

    private val _getHandicraftsState = MutableStateFlow(HomeViewState())
    val getHandicraftsState = _getHandicraftsState

    init {
        getHandicrafts()
        getUsername()
        getAllHandicraft()
    }

    private fun getAllHandicraft() {
        viewModelScope.launch {
            getAllHistoryUseCase().collect { result ->
                _getHandicraftsState.update { it.copy(resultGetHistory = result) }
            }
        }
    }

    fun getHandicrafts() {
        viewModelScope.launch {
            handicraftUseCase().collect { result ->
                _getHandicraftsState.update { it.copy(resultGetHandicrafts = result) }
            }
        }
    }

    private fun getUsername() {
        viewModelScope.launch {
            getUserUseCase().collect { user ->
                _getHandicraftsState.update {
                    it.copy(username = user.name)
                }
            }
        }
    }

    class Factory(
        private val handicraftUseCase: GetAllHandicraftUseCase,
        private val getUserUseCase: GetUserUseCase,
        private val getAllHistoryUseCase: GetAllHistoryUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(handicraftUseCase, getUserUseCase, getAllHistoryUseCase) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }

}

