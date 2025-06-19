package com.app.flixtrain.di

import com.app.flixtrain.data.repository.TaskRepositoryImpl
import com.app.flixtrain.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
     */
    @Binds
    @Singleton
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}
