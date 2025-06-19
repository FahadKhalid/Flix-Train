package com.app.flixtrain.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.flixtrain.data.local.TaskDao
import com.app.flixtrain.data.remote.TaskApiService
import com.app.flixtrain.data.util.toDomainModels
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

/**
 * This class is responsible for coordinating data flow between the remote API
 * and the local Room database, providing an offline-first strategy.
 */
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService,
    private val connectivityManager: ConnectivityManager,
    @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    /**
     * Checks if the device currently has active network connectivity.
     */
    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun getTasks(): Flow<List<Task>> = flow {
        // Emit data from local database first (offline-first principle)
        taskDao.getAllTasks().collect { cachedTasks ->
            emit(cachedTasks)
        }

    }.flowOn(ioDispatcher)

    /**
     * Retrieves a single task by its ID from the local database.
     */
    override fun getTaskById(taskId: String): Flow<Task?> {
        return taskDao.getTaskById(taskId).flowOn(ioDispatcher)
    }

    /**
     * Triggers a synchronization of tasks from the remote source to the local database.
     */
    override suspend fun syncTasks() {
        withContext(ioDispatcher) {
            if (!isNetworkAvailable()) {
                throw IOException("No internet connection available. Cannot sync data.")
            }

            try {
                val response = taskApiService.getTasks()
                if (response.isSuccessful) {
                    response.body()?.tasks?.let { taskDtos ->
                        val tasks = taskDtos.toDomainModels()
                        taskDao.deleteAllTasks()
                        taskDao.insertAll(tasks)
                    } ?: run {
                        throw IOException("Server returned empty data.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "API call failed with code: ${response.code()}, error: $errorBody"
                    println(errorMessage)
                    throw HttpException(response)
                }
            } catch (e: HttpException) {
                println("HTTP Exception during sync: ${e.message()}")
                throw e
            } catch (e: IOException) {
                println("Network/IO Error during sync: ${e.message}")
                throw e
            } catch (e: Exception) {
                println("An unexpected error occurred during sync: ${e.message}")
                throw e
            }
        }
    }
}