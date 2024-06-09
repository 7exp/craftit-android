package com.sevenexp.craftit.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.domain.usecase.GetUserUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    data class MainActivityViewState(
        val resultGetUser: ResultState<Boolean> = ResultState.Idle(),
        val username: String = ""
    )

    private val _isLoggedIn = MutableStateFlow(MainActivityViewState())
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        getIsLoggedIn()
    }

    private fun getIsLoggedIn() {
        viewModelScope.launch {
            getUserUseCase().collect { user ->
                _isLoggedIn.update { it.copy(resultGetUser = ResultState.Success(user.token.isNotEmpty())) }
            }
        }
    }


    class Factory(
        private val getUserUseCase: GetUserUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
                return WelcomeViewModel(getUserUseCase) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }
}