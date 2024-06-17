package com.sevenexp.craftit.domain.interfaces

import com.sevenexp.craftit.data.response.CreateUserResponse
import com.sevenexp.craftit.data.response.LoginResponse
import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AuthRepositoryInterface {
    fun register(name: String, email: String, password: String): Flow<CreateUserResponse>
    fun login(email: String, password: String): Flow<LoginResponse>
    fun updateProfilePicture(image:File): Flow<UpdateProfilePictureResponse>
}