package com.app.flixtrain.data.util

import com.app.flixtrain.data.remote.model.TaskDto
import com.app.flixtrain.domain.model.Task

/**
 * Extension function to convert a single [TaskDto] (Data Transfer Object from network)
 */
fun TaskDto.toDomainModel(): Task {
    return Task(
        taskId = this.taskId,
        trainId = this.trainId,
        taskType = this.taskType,
        priorityLevel = this.priorityLevel,
        location = this.location,
        dueDate = this.dueDate,
        description = this.description
    )
}

/**
 * Extension function to convert a list of [TaskDto] objects to a list of [Task] objects.
 */
fun List<TaskDto>.toDomainModels(): List<Task> {
    return this.map { it.toDomainModel() }
}