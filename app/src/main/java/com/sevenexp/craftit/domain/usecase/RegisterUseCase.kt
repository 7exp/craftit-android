package com.sevenexp.craftit.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

data class ErrorResponse(val message: String)

class RegisterUseCase(
    private val context: Context,
    private val authRepository: AuthRepository
) {
    operator fun invoke(name: String, email: String, password: String): Flow<ResultState<String>> =
        flow {
            emit(ResultState.Loading())
            try {
                authRepository.register(name, email, password).collect { res ->
                    if (res.data != null) {
                        emit(ResultState.Success(res.message))
                    } else {
                        emit(ResultState.Error(res.message))
                    }
                }
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    emit(ResultState.Error(context.getString(R.string.err_email_already_registered)))
                } else {
                    val errorJsonString = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    emit(ResultState.Error(errorResponse.message))
                }
            }
        }
}