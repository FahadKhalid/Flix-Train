package com.app.flixtrain.presentation.viewmodel

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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val syncTasksUseCase: SyncTasksUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    // MutableStateFlow to hold the current UI state of tasks
    private val _tasksUiState = MutableStateFlow<UiState<List<Task>>>(UiState.Loading)
    val tasksUiState: StateFlow<UiState<List<Task>>> = _tasksUiState.asStateFlow()

    // MutableStateFlow to indicate if the app is currently in offline mode (network status)
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    init {
        observeNetworkConnectivity()
        loadInitialTasks()
    }

    // Observe network connectivity changes
    private fun observeNetworkConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isConnected ->
                    handleNetworkStateChange(isConnected)
                }
        }
    }

    // Handle network state changes (offline/online)
    private fun handleNetworkStateChange(isConnected: Boolean) {
        _isOfflineMode.value = !isConnected
        if (isConnected) {
            attemptToRefreshTasks()
        } else {
            // Optional: Handle cases for offline
            Timber.d("App is offline, showing cached data.")
        }
    }

    // Try refreshing tasks if connected to network
    private fun attemptToRefreshTasks() {
        if (_tasksUiState.value is UiState.Loading) return

        _tasksUiState.value = UiState.Loading
        refreshTasks()
    }
    // Fetch tasks from database and update UI
    private fun loadInitialTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().collect { tasks ->
                if (tasks.isEmpty()) {
                    Timber.d("No tasks in DB, trying to sync from remote.")
                    refreshTasks()
                } else {
                    updateTasksState(tasks)
                }
            }
        }
    }

    // Update the tasks UI state based on the fetched data
    private fun updateTasksState(tasks: List<Task>) {
        _tasksUiState.value = if (tasks.isEmpty()) {
            UiState.Error("No tasks found.")
        } else {
            UiState.Success(tasks)
        }
    }

    // Refresh tasks from the remote source and update DB
    private fun refreshTasks() {
        viewModelScope.launch {
            try {
                syncTasksUseCase() // Sync tasks with the remote source
                // This will automatically update UI state from collectTasks() as tasks are updated in DB
            } catch (e: Exception) {
                _tasksUiState.value = UiState.Error("Failed to sync tasks: ${e.message}")
            }
        }
    }
}
