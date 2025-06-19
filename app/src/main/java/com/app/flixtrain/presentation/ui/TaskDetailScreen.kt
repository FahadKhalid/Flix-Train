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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.flixtrain.domain.model.Task
import com.app.flixtrain.presentation.TaskDetailViewModel
import com.app.flixtrain.ui.theme.FlixTrainMaintainanceTrackerAppTheme
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Composable function for displaying the detailed information of a single maintenance task.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    // Collect the task from the ViewModel as Compose State
    val task by viewModel.task.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        task?.taskType ?: "Task Details"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
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

                task?.let { currentTask ->
                    // If 'task' is not null, 'currentTask' will be non-nullable within this block
                    TaskDetailsCard(task = currentTask)
                } ?: run {
                    // This block executes if 'task' is null
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Loading task or Task not found.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DetailRow("Task ID:", task.taskId)
            DetailRow("Train ID:", task.trainId)
            DetailRow("Location:", task.location)
            DetailRow("Due Date:", formatDate(task.dueDate))
            DetailRow("Priority:", task.priorityLevel)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Description:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
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
