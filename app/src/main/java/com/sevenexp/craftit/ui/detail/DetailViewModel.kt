package com.sevenexp.craftit.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.data.response.items.StepItem
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.domain.usecase.GetDetailUseCase
import com.sevenexp.craftit.domain.usecase.InsertHistoryUseCase
import com.sevenexp.craftit.domain.usecase.UpdateStepUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDetailUseCase: GetDetailUseCase,
    private val updateStepUseCase: UpdateStepUseCase,
    private val insertHistoryUseCase: InsertHistoryUseCase
) : ViewModel() {
    data class DetailState(
        val resultGetDetail: ResultState<DetailResponse> = ResultState.Idle(),
        val resultStep: ResultState<StepItem> = ResultState.Idle()
    )

    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    fun getDetail(id: String) {
        viewModelScope.launch {
            getDetailUseCase(id).collect { detail ->
                _detailState.value = _detailState.value.copy(resultGetDetail = detail)
            }
        }
    }

    fun insertHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            insertHistoryUseCase(historyEntity)
        }
    }

    fun updateStep(stepItem: StepItem, historyEntity: HistoryEntity) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(resultStep = ResultState.Success(stepItem))

            updateStepUseCase(historyEntity)
        }
    }

    class Factory(
        private val getDetailUseCase: GetDetailUseCase,
        private val updateStepUseCase: UpdateStepUseCase,
        private val insertHistoryUseCase: InsertHistoryUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(
                    getDetailUseCase,
                    updateStepUseCase,
                    insertHistoryUseCase
                ) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }
}