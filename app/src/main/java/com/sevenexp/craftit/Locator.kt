package com.sevenexp.craftit

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.sevenexp.craftit.data.repository.AuthRepository
import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.repository.HistoryRepository
import com.sevenexp.craftit.data.source.database.HandicraftDatabase
import com.sevenexp.craftit.data.source.local.UserPreferences
import com.sevenexp.craftit.data.source.remote.ApiConfig
import com.sevenexp.craftit.domain.usecase.GetAllHistoryUseCase
import com.sevenexp.craftit.domain.usecase.GetDetailUseCase
import com.sevenexp.craftit.domain.usecase.GetFypUseCase
import com.sevenexp.craftit.domain.usecase.GetUserDetailUseCase
import com.sevenexp.craftit.domain.usecase.GetUserUseCase
import com.sevenexp.craftit.domain.usecase.InsertHistoryUseCase
import com.sevenexp.craftit.domain.usecase.LoginUseCase
import com.sevenexp.craftit.domain.usecase.RegisterUseCase
import com.sevenexp.craftit.domain.usecase.SearchUseCase
import com.sevenexp.craftit.domain.usecase.UpdatePictureUseCase
import com.sevenexp.craftit.domain.usecase.UpdateStepUseCase
import com.sevenexp.craftit.ui.auth.login.LoginViewModel
import com.sevenexp.craftit.ui.auth.register.RegisterViewModel
import com.sevenexp.craftit.ui.detail.DetailViewModel
import com.sevenexp.craftit.ui.home.HomeViewModel
import com.sevenexp.craftit.ui.search.SearchViewModel
import com.sevenexp.craftit.ui.update_picture.UpdateProfilePictureViewModel
import com.sevenexp.craftit.ui.welcome.WelcomeViewModel

object Locator {
    private var application: Application? = null

    private inline val requireApplication
        get() = application
            ?: error("You forgot to call Locator.initWith(application) in your Application class")

    fun initWith(application: Application) {
        this.application = application
    }

    private val Context.datastore by preferencesDataStore(name = "user_preferences")


    private val userPrefRepos by lazy { UserPreferences(requireApplication.datastore) }
    private val authRepos by lazy { AuthRepository(ApiConfig(requireApplication.datastore).getApiService()) }
    private val handicraftRepos by lazy {
        HandicraftRepository(
            ApiConfig(requireApplication.datastore).getApiService(),
            HandicraftDatabase.getDatabase(requireApplication.baseContext),
            userPrefRepos
        )
    }
    private val historyRepos by lazy {
        HistoryRepository(
            HandicraftDatabase.getDatabase(requireApplication.baseContext)
        )
    }


    // ViewModels
    val welcomeViewModelFactory by lazy {
        WelcomeViewModel.Factory(GetUserUseCase(userPrefRepos))
    }
    val registerViewModelFactory by lazy {
        RegisterViewModel.Factory(
            RegisterUseCase(requireApplication.baseContext, authRepos),
            LoginUseCase(authRepos, userPrefRepos)
        )
    }
    val loginViewModelFactory by lazy {
        LoginViewModel.Factory(
            LoginUseCase(authRepos, userPrefRepos)
        )
    }
    val homeViewModelFactory by lazy {
        HomeViewModel.Factory(
            GetFypUseCase(handicraftRepos),
            GetUserUseCase(userPrefRepos),
            GetAllHistoryUseCase(historyRepos),
            GetUserDetailUseCase(authRepos)
        )
    }
    val searchResulViewModelFactory by lazy {
        SearchViewModel.Factory(
            SearchUseCase(handicraftRepos)
        )
    }
    val updateProfilePictureFactory by lazy {
        UpdateProfilePictureViewModel.Factory(
            UpdatePictureUseCase(authRepos, userPrefRepos)
        )
    }
    val DetailViewModelFactory by lazy {
        DetailViewModel.Factory(
            GetDetailUseCase(handicraftRepos),
            UpdateStepUseCase(historyRepos),
            InsertHistoryUseCase(historyRepos)
        )
    }
}