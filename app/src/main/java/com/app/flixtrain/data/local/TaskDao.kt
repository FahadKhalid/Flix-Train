package com.app.flixtrain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Task entity.
 * Defines methods for interacting with the 'tasks' table in the Room database.
 */
@Dao
interface TaskDao {

    /**
     * Retrieves all tasks from the database.
     */
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC, priorityLevel DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Retrieves a single task by its ID.
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Flow<Task?>

    /**
     * Inserts a list of tasks into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>)

    /**
     * Deletes all tasks from the database.
     * Useful for clearing old data before a fresh sync.
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}