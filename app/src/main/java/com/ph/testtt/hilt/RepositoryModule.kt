package com.ph.testtt.hilt

import com.ph.testtt.data.repository.RandomStringRepository
import com.ph.testtt.data.room.RandomStringDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(dao: RandomStringDao): RandomStringRepository {
        return RandomStringRepository(dao)
    }
}