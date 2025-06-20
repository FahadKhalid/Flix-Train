package com.app.flixtrain.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.app.flixtrain.data.local.TaskDao
import com.app.flixtrain.data.remote.TaskApiService
import com.app.flixtrain.data.remote.model.TaskDto
import com.app.flixtrain.data.util.toDomainModels
import com.app.flixtrain.domain.core.Results
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
import timber.log.Timber

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService,
    private val connectivityManager: ConnectivityManager,
    @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> = flow {
        taskDao.getAllTasks().collect { emit(it) }
    }.flowOn(ioDispatcher)

    override fun getTaskById(taskId: String): Flow<Task> {
        return taskDao.getTaskById(taskId).flowOn(ioDispatcher)
    }

    override suspend fun syncTasks(): Results<Unit> = withContext(ioDispatcher) {
        if (!isNetworkAvailable()) {
            return@withContext Results.Error(IOException())
        }

        return@withContext try {
            val response = taskApiService.getTasks()
            if (response.isSuccessful) {
                val taskDtos = response.body()?.tasks
                if (taskDtos.isNullOrEmpty()) {
                    Results.Error(IOException("Empty response from server"))
                } else {
                    replaceAllTasks(taskDtos)
                    Results.Success(Unit)
                }
            } else {
                Results.Error(HttpException(response))
            }
        } catch (e: Exception) {
            Timber.e(e, "Sync failed")
            Results.Error(e)
        }
    }

    private suspend fun replaceAllTasks(taskDtos: List<TaskDto>) {
        try {
            val tasks = taskDtos.toDomainModels()
            taskDao.replaceAllTasks(tasks)
        } catch (e: Exception) {
            Timber.e(e, "Failed to replace tasks in the local database")
        }
    }

    private suspend fun isNetworkAvailable(): Boolean {
        return withContext(ioDispatcher) {
            val network = connectivityManager.activeNetwork ?: return@withContext false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return@withContext false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}