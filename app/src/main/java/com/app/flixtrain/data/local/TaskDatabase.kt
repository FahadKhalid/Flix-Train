package com.app.flixtrain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.flixtrain.domain.model.Task

/**
 * The Room database for the Train Maintenance Tracker application.
 * Defines the database schema and provides access to the DAOs.
 */
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}