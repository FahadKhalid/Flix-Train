package com.app.flixtrain.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.app.flixtrain.presentation.viewmodel.TaskDetailViewModel
import com.app.flixtrain.presentation.theme.FlixTrainMaintainanceTrackerAppTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.flixtrain.R
import com.app.flixtrain.presentation.common.UiState

/**
 * Composable function for displaying the detailed information of a single maintenance task.
 */
@Composable
fun TaskDetailScreen(
    navController: NavController,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val taskState by viewModel.taskState.collectAsState()

    Scaffold(
        topBar = {
            TaskDetailTopAppBar(
                navController = navController,
                title = when (taskState) {
                    is UiState.Success -> (taskState as UiState.Success).data.taskType
                    else -> stringResource(R.string.task_details)
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                when (taskState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.loading_tasks), style = MaterialTheme.typography.bodyLarge)
                    }
                    is UiState.Success -> {
                        TaskDetailsCard(task = (taskState as UiState.Success).data)
                    }
                    is UiState.Error -> {
                        val errorMessage = (taskState as UiState.Error).message ?: stringResource(R.string.error_message)
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailTopAppBar(navController: NavController, title: String) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

/**
 * Composable card to display detailed information.
 */
@Composable
fun TaskDetailsCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = task.taskType,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp) // Increased bottom padding
            )
            DetailRow(stringResource(R.string.TaskId), task.taskId)
            DetailRow(stringResource(R.string.TrainId), task.trainId)
            DetailRow(stringResource(R.string.Location), task.location)
            DetailRow(stringResource(R.string.DueDate), formatDate(task.dueDate))
            DetailRow(stringResource(R.string.Priority), task.priorityLevel)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.description_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp) // Slightly more padding
            )
        }
    }
}

/**
 * Helper Composable for displaying a single row of detail.
 */
@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    FlixTrainMaintainanceTrackerAppTheme {
        val sampleTask = Task(
            "1",
            "T101",
            "Brake Inspection",
            "High",
            "Depot A",
            "2025-07-15",
            "Detailed check of brake pads for wear, fluid levels, and overall system integrity. Includes a test run."
        )
        Column {
            TopAppBar(
                title = { Text(sampleTask.taskType + " (Preview)") },
                navigationIcon = {
                    IconButton(onClick = { /* No-op */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
            TaskDetailsCard(task = sampleTask)
        }
    }
}
