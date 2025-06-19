package com.app.flixtrain.presentation.common

/**
 * A sealed class representing the various states of UI data loading,
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T, val isOfflineCache: Boolean = false) : UiState<T>()
    data class Error<out T>(val message: String? = null, val cachedData: T? = null) : UiState<T>()
}
