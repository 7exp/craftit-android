package com.sevenexp.craftit.data.source.remote

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sevenexp.craftit.data.source.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = runBlocking {
            dataStore.data.first()[UserPreferences.Keys.TOKEN]
        }

        Log.d("AuthInterceptor", "Token: $token")

        return if (!token.isNullOrEmpty()) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            Log.d("AuthInterceptor", "New Request: $newRequest")
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}