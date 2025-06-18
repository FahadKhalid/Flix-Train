package com.app.flixtrain.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey // Marks 'id' as the primary key for the database table
    val id: String,
    val trainId: String,
    val taskType: String,
    val priorityLevel: String,
    val location: String,
    val dueDate: String,
    val description: String
)