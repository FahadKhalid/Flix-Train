package com.app.flixtrain.di

import com.app.flixtrain.BuildConfig
import com.app.flixtrain.data.remote.TaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Used for logging HTTP request and response data, which is very helpful for debugging.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }
    }

    /**
     * Configures the HTTP client with timeouts and adds the logging interceptor.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Write timeout
            .build()
    }

    /**
     * Configures Retrofit with the base URL, OkHttpClient, and Gson converter factory.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Set the base URL for the API
            .client(okHttpClient) // Use the configured OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson for JSON serialization/deserialization
            .build()
    }

    /**
     * Creates the API service using the Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideTaskApiService(retrofit: Retrofit): TaskApiService {
        return retrofit.create(TaskApiService::class.java)
    }
}
