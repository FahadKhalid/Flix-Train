package com.app.flixtrain.domain.repository

import com.app.flixtrain.domain.core.Results
import com.app.flixtrain.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * Retrieves all tasks.
     */
    fun getTasks(): Flow<List<Task>>

    /**
     * Retrieves a single task by its ID.
     */
    fun getTaskById(taskId: String): Flow<Task>

    /**
     * Triggers a synchronization of tasks from the remote source to the local database.
     * Returns Result to indicate success or failure.
     */
    suspend fun syncTasks(): Results<Unit>
}
