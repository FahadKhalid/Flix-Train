package com.app.flixtrain.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.presentation.navigation.Screen
import com.app.flixtrain.ui.theme.FlixTrainMaintainanceTrackerAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.flixtrain.R
import com.app.flixtrain.presentation.common.UiState
import com.app.flixtrain.presentation.viewmodel.TasksViewModel

/**
 * Composable function for displaying the list of train maintenance tasks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    // Collect states from the ViewModel as Compose State
    val tasksUiState by viewModel.tasksUiState.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maintenance Tasks") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isOfflineMode) {
                    Text(
                        stringResource(R.string.currently_offline_cached_data),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Handling the UI states
                when (tasksUiState) {
                    is UiState.Loading -> {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.loading_tasks), style = MaterialTheme.typography.bodyMedium)
                    }

                    is UiState.Success -> {
                        val tasks =
                            (tasksUiState as UiState.Success<List<Task>>).data
                        if (tasks.isEmpty()) {
                            if (isOfflineMode) {
                                Text(
                                    stringResource(R.string.offline_no_data_initial_sync),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(tasks) { task ->
                                    TaskItem(task = task) { taskId ->
                                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                                    }
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        Text(
                            "Error: ${(tasksUiState as UiState.Error).message ?: "Unknown error"}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

/**
 * Composable for displaying a single task item in the list.
 */
@Composable
fun TaskItem(task: Task, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(task.taskId) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.taskType,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Train: ${task.trainId}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Priority: ${task.priorityLevel}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Helper function to format the due date string for display.
 * Assumes input date format is "yyyy-MM-dd" and outputs "MMM dd, yyyy".
 */
fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date: Date = parser.parse(dateString) ?: Date()
        formatter.format(date)
    } catch (e: Exception) {
        dateString // Return original if parsing fails
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    FlixTrainMaintainanceTrackerAppTheme {
        // Provide dummy data for preview
        val sampleTasks = listOf(
            Task(
                "1",
                "T101",
                "Brake Inspection",
                "High",
                "Depot A",
                "2025-07-15",
                "Check brake pads and fluid levels."
            ),
            Task(
                "2",
                "T205",
                "Engine Check",
                "Medium",
                "Yard B",
                "2025-07-20",
                "Routine engine diagnostic."
            ),
            Task(
                "3",
                "T310",
                "Wheel Alignment",
                "Low",
                "Maintenance Bay",
                "2025-07-22",
                "Adjust wheel alignment for smooth operation."
            )
        )
        // In a real preview, you might mock the ViewModel's state directly
        Column {
            TopAppBar(title = { Text("Train Maintenance Tasks (Preview)") })
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(sampleTasks) { task ->
                    TaskItem(task = task) { /* No-op for preview click */ }
                }
            }
        }
    }
}
