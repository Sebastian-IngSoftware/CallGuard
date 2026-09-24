package com.callguard.app.domain.model

enum class MatchType {
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    EXACT
}

data class BlacklistRule(
    val id: Long = 0L,
    val pattern: String,
    val matchType: MatchType,
    val isEnabled: Boolean = true
)
