package com.app.flixtrain.data.remote.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("train_id")
    val trainId: String,
    @SerializedName("task_type")
    val taskType: String,
    @SerializedName("priority_level")
    val priorityLevel: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("due_date")
    val dueDate: String,
    @SerializedName("description")
    val description: String
)