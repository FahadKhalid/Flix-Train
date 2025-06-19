package com.app.flixtrain.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the top-level structure of the tasks API response.
 * The list of actual tasks is nested under the "tasks" key.
 */
data class TasksResponse(
    @SerializedName("tasks")
    val tasks: List<TaskDto>
)