package com.callguard.app.di

import com.callguard.app.data.repository.BlacklistRepository
import com.callguard.app.data.repository.BlacklistRepositoryImpl
import com.callguard.app.data.repository.NewsRepository
import com.callguard.app.data.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBlacklistRepository(impl: BlacklistRepositoryImpl): BlacklistRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository
}
