package com.app.flixtrain.presentation.navigation

/**
 * Sealed class defining the navigation routes in the application.
 */
sealed class Screen(val route: String) {
    /**
     * Route for the list of maintenance tasks.
     */
    data object TasksList : Screen(NavigationConstants.ROUTE_TASKS_LIST)

    /**
     * Route for the detailed view of a single task.
     */
    data object TaskDetail :
        Screen("${NavigationConstants.ROUTE_TASK_DETAIL}/{${NavigationConstants.ARG_TASK_ID}}") {
        /**
         * Creates the full route for navigating to a specific task detail.
         */
        fun createRoute(taskId: String) = "${NavigationConstants.ROUTE_TASK_DETAIL}/$taskId"
    }
}
