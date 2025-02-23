package com.ph.testtt.hilt

import android.content.Context
import com.ph.testtt.data.provider.RandomStringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ProvidersModule {

    @Provides
    fun provideRandomStringProvider(@ApplicationContext context: Context): RandomStringProvider {
        return RandomStringProvider(context)
    }
}