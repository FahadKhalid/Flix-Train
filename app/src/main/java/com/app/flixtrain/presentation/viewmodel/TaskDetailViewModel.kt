package com.app.flixtrain.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.usecase.GetTaskByIdUseCase
import com.app.flixtrain.presentation.navigation.NavigationConstants.ARG_TASK_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.app.flixtrain.presentation.common.UiState

/**
 * ViewModel for displaying the details of a single maintenance task.
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _taskState = MutableStateFlow<UiState<Task>>(UiState.Loading)
    val taskState: StateFlow<UiState<Task>> = _taskState.asStateFlow()

    init {
        val taskId = savedStateHandle.get<String>(ARG_TASK_ID)
        if (taskId != null) {
            loadTaskDetails(taskId)
        } else {
            _taskState.value = UiState.Error("Invalid task ID")
        }
    }

    fun loadTaskDetails(taskId: String) {
        _taskState.value = UiState.Loading
        viewModelScope.launch {
            try {
                getTaskByIdUseCase(taskId).collectLatest { task ->
                    _taskState.value = UiState.Success(data = task)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(exception: Exception) {
        Timber.e(exception, "Error fetching task details")
        _taskState.value = UiState.Error("Failed to load task details. Please try again.")
    }
}