package com.sevenexp.craftit.data.repository

import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.domain.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AuthRepository(private val apiService: ApiService) : AuthRepositoryInterface {
    override fun register(name: String, email: String, password: String) = flow {
        emit(apiService.register(name, email, password))
    }.flowOn(Dispatchers.IO)

    override fun login(email: String, password: String) = flow {
        emit(apiService.login(email, password))
    }.flowOn(Dispatchers.IO)
}