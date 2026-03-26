package com.architect.banking.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.architect.banking.engine.navigation.Routes

/**
 * Adds the Login feature's composable destinations to the [NavGraphBuilder].
 *
 * Call from the root [NavHost] in `:app` — features never access the NavHost directly.
 *
 * @param navController Passed through to [LoginScreen] for effect-driven navigation.
 */
fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable(route = Routes.LOGIN) {
        LoginScreen(navController = navController)
    }
}
