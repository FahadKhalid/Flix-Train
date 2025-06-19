package com.app.flixtrain.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.domain.usecase.GetTaskByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for displaying the details of a single maintenance task.
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // MutableStateFlow to hold the selected task details. Nullable as task might not be found.
    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    init {
        // Retrieve the taskId from navigation arguments
        val taskId = savedStateHandle.get<String>("taskId")

        taskId?.let { id ->
            viewModelScope.launch {
                // Collect the task from the use case. collectLatest cancels previous collection
                getTaskByIdUseCase(id).collectLatest { fetchedTask ->
                    _task.value = fetchedTask
                }
            }
        } ?: run {
            println("Error: taskId is null in TaskDetailViewModel.")
        }
    }
}
