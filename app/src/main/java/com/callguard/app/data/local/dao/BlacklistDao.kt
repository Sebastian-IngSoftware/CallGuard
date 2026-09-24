package com.callguard.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.callguard.app.data.local.entity.BlacklistRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistDao {

    @Query("SELECT * FROM blacklist_rules ORDER BY id DESC")
    fun observeAll(): Flow<List<BlacklistRuleEntity>>

    @Query("SELECT * FROM blacklist_rules")
    suspend fun getAllOnce(): List<BlacklistRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: BlacklistRuleEntity): Long

    @Update
    suspend fun update(rule: BlacklistRuleEntity)

    @Delete
    suspend fun delete(rule: BlacklistRuleEntity)
}
