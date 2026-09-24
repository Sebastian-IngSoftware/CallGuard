package com.callguard.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.callguard.app.domain.model.MatchType

@Entity(tableName = "blacklist_rules")
data class BlacklistRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val pattern: String,
    val matchType: MatchType,
    val isEnabled: Boolean = true
)
