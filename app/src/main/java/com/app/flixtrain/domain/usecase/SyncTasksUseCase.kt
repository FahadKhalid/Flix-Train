package com.app.flixtrain.domain.usecase

import com.app.flixtrain.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Use case for synchronizing (refreshing) train maintenance tasks from a remote source
 * to the local database.
 */
class SyncTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() {
        repository.syncTasks()
    }
}
