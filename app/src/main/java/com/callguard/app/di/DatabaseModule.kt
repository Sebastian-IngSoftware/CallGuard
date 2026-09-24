package com.callguard.app.di

import android.content.Context
import androidx.room.Room
import com.callguard.app.data.local.CallGuardDatabase
import com.callguard.app.data.local.dao.BlacklistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CallGuardDatabase =
        Room.databaseBuilder(
            context,
            CallGuardDatabase::class.java,
            "callguard.db"
        ).build()

    @Provides
    fun provideBlacklistDao(database: CallGuardDatabase): BlacklistDao =
        database.blacklistDao()
}
