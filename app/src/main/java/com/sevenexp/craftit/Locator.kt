package com.sevenexp.craftit

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.data.source.local.UserPreferences
import com.sevenexp.craftit.data.source.remote.ApiConfig
import com.sevenexp.craftit.domain.usecase.GetUserUseCase
import com.sevenexp.craftit.domain.usecase.RegisterUseCase
import com.sevenexp.craftit.ui.auth.register.RegisterViewModel
import com.sevenexp.craftit.ui.welcome.WelcomeViewModel

object Locator {
    private var application: Application? = null

    private inline val requireApplication get() = application ?: error("You forgot to call Locator.initWith(application) in your Application class")

    fun initWith(application: Application) {
        this.application = application
    }

    private val Context.datastore by preferencesDataStore(name = "user_preferences")

    private val userPrefRepos by lazy { UserPreferences(requireApplication.datastore) }
    private val authRepos by lazy { AuthRepository(ApiConfig(requireApplication.datastore).getApiService()) }

    // ViewModels
    val welcomeViewModelFactory by lazy { WelcomeViewModel.Factory(GetUserUseCase(userPrefRepos)) }
    val registerViewModelFactory by lazy { RegisterViewModel.Factory(RegisterUseCase(authRepos)) }
}