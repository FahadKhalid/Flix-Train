package com.app.flixtrain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey // Marks 'taskId' as the primary key for the database table
    val taskId: String,
    val trainId: String,
    val taskType: String,
    val priorityLevel: String,
    val location: String,
    val dueDate: String,
    val description: String
)