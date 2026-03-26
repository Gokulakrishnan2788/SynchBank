package com.architect.banking.feature.dashboard

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.architect.banking.engine.navigation.Routes

/**
 * Adds the Dashboard feature's composable destinations to the [NavGraphBuilder].
 *
 * @param navController Passed through to [DashboardScreen] for effect-driven navigation.
 */
fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable(route = Routes.DASHBOARD) {
        DashboardScreen(navController = navController)
    }
}
