package com.app.flixtrain.data.remote

import com.app.flixtrain.data.remote.model.TasksResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API service interface for fetching train maintenance tasks.
 */
interface TaskApiService {
    @GET("dfa58b73b5fea4951276f89e09b4267d81c0895b/tasks.kt")
    suspend fun getTasks(): Response<TasksResponse>
}