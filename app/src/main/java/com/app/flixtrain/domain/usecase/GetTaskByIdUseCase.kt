package com.app.flixtrain.domain.usecase


import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a single train maintenance task by its ID.
 */
class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(taskId: String): Flow<Task> {
        return repository.getTaskById(taskId)
    }
}