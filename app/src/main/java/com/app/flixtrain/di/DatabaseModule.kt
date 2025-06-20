package com.app.flixtrain.di

import android.content.Context
import androidx.room.Room
import com.app.flixtrain.data.local.TaskDatabase
import com.app.flixtrain.data.local.TaskDao
import com.app.flixtrain.data.local.constants.DatabaseConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides database-related dependencies, such as the Room database instance
 * and the TaskDao.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: TaskDatabase): TaskDao {
        return appDatabase.taskDao()
    }
}