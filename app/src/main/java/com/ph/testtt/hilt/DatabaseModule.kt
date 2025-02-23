package com.ph.testtt.hilt

import android.content.Context
import androidx.room.Room
import com.ph.testtt.data.room.RandomStringDao
import com.ph.testtt.data.room.RandomStringDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): RandomStringDatabase {
        return Room.databaseBuilder(
            context,
            RandomStringDatabase::class.java,
            "random_string_db"
        ).build()
    }

    @Provides
    fun provideDao(database: RandomStringDatabase): RandomStringDao {
        return database.randomStringDao()
    }



}
