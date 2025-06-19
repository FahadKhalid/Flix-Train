package com.app.flixtrain.data.remote.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("taskId")
    val taskId: String,
    @SerializedName("trainId")
    val trainId: String,
    @SerializedName("taskType")
    val taskType: String,
    @SerializedName("priorityLevel")
    val priorityLevel: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("description")
    val description: String
)