package com.callguard.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.callguard.app.data.local.dao.BlacklistDao
import com.callguard.app.data.local.entity.BlacklistRuleEntity

@Database(
    entities = [BlacklistRuleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CallGuardDatabase : RoomDatabase() {
    abstract fun blacklistDao(): BlacklistDao
}
