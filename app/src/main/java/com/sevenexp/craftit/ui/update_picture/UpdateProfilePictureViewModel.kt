package com.sevenexp.craftit.ui.update_picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevenexp.craftit.data.response.UpdateProfilePictureResponse
import com.sevenexp.craftit.domain.usecase.UpdatePictureUseCase
import com.sevenexp.craftit.ui.home.HomeViewModel
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class UpdateProfilePictureViewModel(
    private val updatePictureUseCase: UpdatePictureUseCase
) : ViewModel() {
    data class UpdateViewState(
        val updateResult: ResultState<UpdateProfilePictureResponse> = ResultState.Idle()
    )

    private val _getUpdateState = MutableStateFlow(UpdateViewState())
    val updateState = _getUpdateState

    fun updateImage(image: File) {
        viewModelScope.launch {
            updatePictureUseCase(image).collect {
                _getUpdateState.update { it.copy(updateResult = it.updateResult) }
            }
        }
    }

    class Factory(
        private val updatePictureUseCase: UpdatePictureUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return UpdateProfilePictureViewModel(updatePictureUseCase) as T
            }
            error("Unknown ViewModel class: $modelClass")
        }
    }
}