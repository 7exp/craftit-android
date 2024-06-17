package com.sevenexp.craftit.data.repository

import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.data.source.remote.ApiService
import com.sevenexp.craftit.data.source.remote.request.LoginRequest
import com.sevenexp.craftit.data.source.remote.request.RegisterRequest
import com.sevenexp.craftit.domain.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class AuthRepository(private val apiService: ApiService) : AuthRepositoryInterface {
    override fun register(name: String, email: String, password: String) = flow {
        emit(apiService.register(RegisterRequest(name, email, password)))
    }.flowOn(Dispatchers.IO)

    override fun login(email: String, password: String) = flow {
        emit(apiService.login(LoginRequest(email, password)))
    }.flowOn(Dispatchers.IO)

    override fun updateProfilePicture(image: File): Flow<UpdateProfilePictureResponse> = flow {
        val body = MultipartBody.Part.createFormData(
            "image",
            image.name,
            image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        emit(apiService.updateProfilePicture(image = body))
    }.flowOn(Dispatchers.IO)
}