package com.app.flixtrain.di

import com.app.flixtrain.data.repository.TaskRepositoryImpl
import com.app.flixtrain.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import javax.inject.Named // Import Named for dispatcher injection

/**
 * Hilt module that provides repository implementations and Dispatchers.
 * Annotated with @InstallIn(SingletonComponent::class) to make its provisions available globally
 * as singletons throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [TaskRepositoryImpl] as the concrete implementation for the [TaskRepository] interface.
     * This allows Hilt to provide [TaskRepositoryImpl] whenever [TaskRepository] is injected.
     *
     * @param taskRepositoryImpl The implementation of the task repository.
     * @return The [TaskRepository] interface instance.
     */
    @Binds
    @Singleton
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}
