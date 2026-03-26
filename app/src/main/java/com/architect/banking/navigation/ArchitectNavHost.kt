package com.architect.banking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.architect.banking.engine.navigation.Routes
import com.architect.banking.feature.dashboard.dashboardGraph
import com.architect.banking.feature.login.loginGraph
import com.architect.banking.feature.profile.profileGraph

/**
 * Root [NavHost] that wires all feature navigation graphs.
 *
 * NavHost lives ONLY in :app. Features expose [NavGraphBuilder] extension
 * functions — this composable assembles them.
 *
 * All routes come from [Routes] — never hardcode route strings here.
 *
 * @param navController The application-level [NavHostController].
 */
@Composable
fun ArchitectNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
    ) {
        loginGraph(navController)
        dashboardGraph(navController)
        profileGraph(navController)
    }
}
