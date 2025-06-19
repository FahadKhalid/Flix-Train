package com.app.flixtrain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.usecase.GetAllTasksUseCase
import com.app.flixtrain.domain.usecase.SyncTasksUseCase
import com.app.flixtrain.presentation.common.UiState
import com.app.flixtrain.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the list of maintenance tasks.
 * It exposes the UI state (list of tasks, loading status, offline mode) to the UI
 * and handles fetching data from the domain layer.
 *
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val syncTasksUseCase: SyncTasksUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // MutableStateFlow to hold the current UI state of tasks
    // Initial state is Loading
    private val _tasksUiState = MutableStateFlow<UiState<List<Task>>>(UiState.Loading)
    val tasksUiState: StateFlow<UiState<List<Task>>> = _tasksUiState.asStateFlow()

    // MutableStateFlow to indicate if the app is currently in offline mode (network status)
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    init {
        // Start observing network connectivity
        observeNetworkConnectivity()
        // Start collecting tasks from the database
        collectTasks()
    }

    /**
     * Observes network connectivity and triggers a refresh when connectivity becomes available.
     */
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged() // Only react when online status actually changes
                .collect { isConnected ->
                    _isOfflineMode.value = !isConnected
                    if (isConnected) {
                        // If network becomes available, attempt to refresh tasks
                        if (_tasksUiState.value !is UiState.Loading) {
                            refreshTasks()
                        }
                    }
                }
        }
    }

    private fun collectTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().collect { fetchedTasks ->
                _tasksUiState.value = UiState.Success(fetchedTasks)
            }
        }
    }

    /**
     * Initiates a data synchronization from the remote source.
     */
    private fun refreshTasks() {
        viewModelScope.launch {
            // Only proceed with refresh if not already loading to prevent multiple concurrent syncs
            if (_tasksUiState.value is UiState.Loading) {
                return@launch
            }

            _tasksUiState.value = UiState.Loading

            try {
                syncTasksUseCase()
                // The tasksUiState will be updated to Success automatically by collectTasks()
            } catch (e: Exception) {
                _tasksUiState.value = UiState.Error(e.message ?: "An unknown error occurred during sync.")
            }
        }
    }
}
