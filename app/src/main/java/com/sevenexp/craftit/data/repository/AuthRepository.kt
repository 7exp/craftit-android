package com.sevenexp.craftit.data.repository

import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.data.source.remote.request.LoginRequest
import com.sevenexp.craftit.data.source.remote.request.RegisterRequest
import com.sevenexp.craftit.domain.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AuthRepository(private val apiService: ApiService) : AuthRepositoryInterface {
    override fun register(name: String, email: String, password: String) = flow {
        emit(apiService.register(RegisterRequest(name, email, password)))
    }.flowOn(Dispatchers.IO)

    override fun login(email: String, password: String) = flow {
        emit(apiService.login(LoginRequest(email, password)))
    }.flowOn(Dispatchers.IO)
}