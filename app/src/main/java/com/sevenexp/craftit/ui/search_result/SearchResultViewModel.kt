package com.sevenexp.craftit.ui.search_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.domain.usecase.SearchUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

class SearchResultViewModel(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    data class SearchState(
        val searchResult: ResultState<List<FypItems>> = ResultState.Idle()
    )

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState

    fun search(query: String? = null, image: File? = null) {
        viewModelScope.launch {
            searchUseCase(query, image).collect { result ->
                _searchState.value = SearchState(searchResult = result)
            }
        }
    }

    class Factory(
        private val searchUseCase: SearchUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
                return SearchResultViewModel(searchUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}