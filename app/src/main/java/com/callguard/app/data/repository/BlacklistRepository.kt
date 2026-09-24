package com.callguard.app.data.repository

import com.callguard.app.domain.model.BlacklistRule
import kotlinx.coroutines.flow.Flow

interface BlacklistRepository {
    fun observeRules(): Flow<List<BlacklistRule>>
    suspend fun getRulesOnce(): List<BlacklistRule>
    suspend fun addRule(rule: BlacklistRule)
    suspend fun updateRule(rule: BlacklistRule)
    suspend fun deleteRule(rule: BlacklistRule)
}
