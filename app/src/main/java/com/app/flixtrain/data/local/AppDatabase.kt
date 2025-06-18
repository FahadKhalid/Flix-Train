package com.app.flixtrain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database for the Train Maintenance Tracker application.
 * Defines the database schema and provides access to the DAOs.
 */
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}