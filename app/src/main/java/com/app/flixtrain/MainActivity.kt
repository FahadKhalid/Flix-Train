package com.app.flixtrain

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.flixtrain.presentation.navigation.Screen
import com.app.flixtrain.presentation.ui.TaskDetailScreen
import com.app.flixtrain.presentation.ui.TasksScreen
import com.app.flixtrain.ui.theme.FlixTrainMaintainanceTrackerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlixTrainMaintainanceTrackerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainMaintenanceAppNavHost()
                }
            }
        }
    }
}

/**
 * The main navigation host for the Train Maintenance Tracker application.
 */
@Composable
fun TrainMaintenanceAppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TasksList.route
    ) {
        // Define the Composable for the Tasks List screen
        composable(route = Screen.TasksList.route) {
            TasksScreen(navController = navController)
        }

        // Define the Composable for the Task Detail screen
        composable(
            route = Screen.TaskDetail.route, // Includes the {taskId} argument
            arguments = listOf(navArgument("taskId") {
                type = NavType.StringType
            }) // Define the argument type
        ) { backStackEntry ->
            // Extract the taskId from the navigation arguments
            val taskId = backStackEntry.arguments?.getString("taskId")
            // Pass taskId to the ViewModel (via Hilt's SavedStateHandle)
            // Hilt handles the ViewModel instantiation and argument injection
            TaskDetailScreen(navController = navController)
        }
    }
}