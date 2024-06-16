package com.sevenexp.craftit.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sevenexp.craftit.domain.entity.UserEntity
import com.sevenexp.craftit.domain.interfaces.UserPreferencesRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferences(private val datastore: DataStore<Preferences>) :
    UserPreferencesRepositoryInterface {
    object Keys {
        val ID = stringPreferencesKey("id")
        val NAME = stringPreferencesKey("name")
        val TOKEN = stringPreferencesKey("token")
    }

    private inline val Preferences.id get() = this[Keys.ID] ?: ""
    private inline val Preferences.name get() = this[Keys.NAME] ?: ""
    private inline val Preferences.token get() = this[Keys.TOKEN] ?: ""

    override val getUserData: Flow<UserEntity> = datastore.data.map { preferences ->
        UserEntity(
            id = preferences.id,
            name = preferences.name,
            token = preferences.token
        )
    }

    override suspend fun saveUserData(userEntity: UserEntity) {
        datastore.edit { preferences ->
            preferences[Keys.ID] = userEntity.id
            preferences[Keys.NAME] = userEntity.name
            preferences[Keys.TOKEN] = userEntity.token
        }
    }

    override suspend fun clearUserData() {
        datastore.edit { it.clear() }
    }

    override suspend fun getUserId(): String = datastore.data.first().id
}