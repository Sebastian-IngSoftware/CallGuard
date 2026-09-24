package com.callguard.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "callguard_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val BLOCK_ALL_UNKNOWN = booleanPreferencesKey("block_all_unknown")
        val PROTECTION_ENABLED = booleanPreferencesKey("protection_enabled")
    }

    val blockAllUnknown: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.BLOCK_ALL_UNKNOWN] ?: false }

    val protectionEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.PROTECTION_ENABLED] ?: true }

    suspend fun setBlockAllUnknown(value: Boolean) {
        context.dataStore.edit { it[Keys.BLOCK_ALL_UNKNOWN] = value }
    }

    suspend fun setProtectionEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.PROTECTION_ENABLED] = value }
    }
}
