package com.callguard.app.ui.blacklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.callguard.app.data.repository.BlacklistRepository
import com.callguard.app.domain.model.BlacklistRule
import com.callguard.app.domain.model.MatchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlacklistViewModel @Inject constructor(
    private val repository: BlacklistRepository
) : ViewModel() {

    val rules: StateFlow<List<BlacklistRule>> = repository.observeRules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addRule(pattern: String, matchType: MatchType) {
        if (pattern.isBlank()) return
        viewModelScope.launch {
            repository.addRule(BlacklistRule(pattern = pattern.trim(), matchType = matchType))
        }
    }

    fun toggleRule(rule: BlacklistRule) {
        viewModelScope.launch {
            repository.updateRule(rule.copy(isEnabled = !rule.isEnabled))
        }
    }

    fun deleteRule(rule: BlacklistRule) {
        viewModelScope.launch {
            repository.deleteRule(rule)
        }
    }
}
