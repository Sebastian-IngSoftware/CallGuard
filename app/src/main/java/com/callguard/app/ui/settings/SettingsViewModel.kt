package com.callguard.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.callguard.app.data.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val protectionEnabled: StateFlow<Boolean> = settingsDataStore.protectionEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val blockAllUnknown: StateFlow<Boolean> = settingsDataStore.blockAllUnknown
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setProtectionEnabled(value: Boolean) {
        viewModelScope.launch { settingsDataStore.setProtectionEnabled(value) }
    }

    fun setBlockAllUnknown(value: Boolean) {
        viewModelScope.launch { settingsDataStore.setBlockAllUnknown(value) }
    }
}
