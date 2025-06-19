package com.app.flixtrain.presentation.navigation

/**
 * Sealed class defining the navigation routes in the application.
 */
sealed class Screen(val route: String) {
    /**
     * Route for the list of maintenance tasks.
     */
    data object TasksList : Screen("tasks_list")

    /**
     * Route for the detailed view of a single task.
     */
    data object TaskDetail : Screen("task_detail/{taskId}") {
        /**
         * Creates the full route for navigating to a specific task detail.
         */
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
}
