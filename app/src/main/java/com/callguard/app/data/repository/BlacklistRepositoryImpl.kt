package com.callguard.app.data.repository

import com.callguard.app.data.local.dao.BlacklistDao
import com.callguard.app.data.local.entity.BlacklistRuleEntity
import com.callguard.app.domain.model.BlacklistRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlacklistRepositoryImpl @Inject constructor(
    private val dao: BlacklistDao
) : BlacklistRepository {

    override fun observeRules(): Flow<List<BlacklistRule>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getRulesOnce(): List<BlacklistRule> =
        dao.getAllOnce().map { it.toDomain() }

    override suspend fun addRule(rule: BlacklistRule) {
        dao.insert(rule.toEntity())
    }

    override suspend fun updateRule(rule: BlacklistRule) {
        dao.update(rule.toEntity())
    }

    override suspend fun deleteRule(rule: BlacklistRule) {
        dao.delete(rule.toEntity())
    }
}

private fun BlacklistRuleEntity.toDomain() = BlacklistRule(
    id = id,
    pattern = pattern,
    matchType = matchType,
    isEnabled = isEnabled
)

private fun BlacklistRule.toEntity() = BlacklistRuleEntity(
    id = id,
    pattern = pattern,
    matchType = matchType,
    isEnabled = isEnabled
)
