package com.sevenexp.craftit.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.domain.usecase.LoginUseCase
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel (
    private val loginUseCase: LoginUseCase
): ViewModel(){
    data class LoginViewState(
        val resultLogin: ResultState<String> = ResultState.Idle()
    )

    private val _loginState = MutableStateFlow(LoginViewState())
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String){
        loginUseCase(email, password).onEach { result ->
            _loginState.value = _loginState.value.copy(resultLogin = result)
        }.launchIn(viewModelScope)
    }

    class Factory(
        private val loginUseCase: LoginUseCase
    ): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(loginUseCase) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }
}