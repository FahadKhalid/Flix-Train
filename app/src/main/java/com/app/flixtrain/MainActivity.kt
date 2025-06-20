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
import com.app.flixtrain.presentation.navigation.NavigationConstants.ARG_TASK_ID
import com.app.flixtrain.presentation.navigation.Route
import com.app.flixtrain.presentation.ui.TaskDetailScreen
import com.app.flixtrain.presentation.ui.TasksScreen
import com.app.flixtrain.presentation.theme.FlixTrainMaintainanceTrackerAppTheme
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
        startDestination = Route.TasksList.route
    ) {
        // Define the Composable for the Tasks List screen
        composable(route = Route.TasksList.route) {
            TasksScreen(navController = navController)
        }

        // Define the Composable for the Task Detail screen
        composable(
            route = Route.TaskDetail.route, // Includes the {taskId} argument
            arguments = listOf(navArgument(ARG_TASK_ID) {
                type = NavType.StringType
            }) // Define the argument type
        ) { backStackEntry ->
            // Extract the taskId from the navigation arguments
            backStackEntry.arguments?.getString(ARG_TASK_ID)
            // Pass taskId to the ViewModel (via Hilt's SavedStateHandle)
            // Hilt handles the ViewModel instantiation and argument injection
            TaskDetailScreen(navController = navController)
        }
    }
}