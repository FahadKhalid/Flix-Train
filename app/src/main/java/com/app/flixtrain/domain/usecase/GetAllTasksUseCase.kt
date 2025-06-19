package com.app.flixtrain.domain.usecase

import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all train maintenance tasks.
 */
class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasks()
    }
}
